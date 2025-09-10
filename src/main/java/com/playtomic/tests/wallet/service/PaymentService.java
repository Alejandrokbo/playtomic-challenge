package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.constants.TransactionConcept;
import com.playtomic.tests.wallet.constants.TransactionStatus;
import com.playtomic.tests.wallet.entity.Card;
import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.CurrencyMismatchException;
import com.playtomic.tests.wallet.exception.InvalidAmountException;
import com.playtomic.tests.wallet.exception.PaymentFailedException;
import com.playtomic.tests.wallet.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.exception.cardException.NotCardFoundException;
import com.playtomic.tests.wallet.model.Payment;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.utils.MoneyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.LockSupport;
import java.time.Duration;

@Service
public class PaymentService {
    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private static final int MAX_RETRIES = 5;
    
    @Autowired
    private WalletService walletService;

    @Autowired
    private StripeService stripe;

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public Transaction processPayment(String walletId, BigDecimal amount, String cardAlias) {
        // Validate and normalize the amount (transaction amount must be > 0)
        BigDecimal normalizedAmount;
        try {
            normalizedAmount = MoneyUtils.validateAndNormalizeTransactionAmount(amount);
        } catch (InvalidAmountException e) {
            log.error("Invalid amount provided: {}", amount, e);
            throw e;
        }
        
        Wallet wallet = walletService.getWallet(walletId);
        
        // Find the card and validate currency
        Card selectedCard = wallet.getCards().stream()
                .filter(card -> card.getAlias().equals(cardAlias))
                .findFirst()
                .orElseThrow(() -> new NotCardFoundException());
        
        // Validate currency matching between wallet and card
        if (!wallet.getCurrency().equals(selectedCard.getCurrency())) {
            throw new CurrencyMismatchException("Wallet currency (" + wallet.getCurrency() + 
                    ") does not match card currency (" + selectedCard.getCurrency() + ")");
        }
        
        // Create transaction
        Transaction transaction = new Transaction(normalizedAmount, TransactionConcept.DEPOSIT.name(), TransactionStatus.PENDING);
        transaction.setCardAlias(cardAlias);
        transaction.setTransactionDate(new Date());
        
        // Charge through Stripe
        Payment payment;
        try {
            payment = stripe.charge(selectedCard.getPan(), normalizedAmount);
        } catch (StripeAmountTooSmallException e) {
            log.error("Stripe amount too small for payment: {}", normalizedAmount, e);
            transaction.setStatus(TransactionStatus.FAILED.name());
            saveTransactionWithRetry(walletId, transaction);
            throw e;
        } catch (Exception e) {
            log.error("Stripe payment failed for amount: {}", normalizedAmount, e);
            transaction.setStatus(TransactionStatus.FAILED.name());
            saveTransactionWithRetry(walletId, transaction);
            throw new PaymentFailedException("Payment processing failed: " + e.getMessage());
        }
        
        // Update transaction with payment details and complete it
        transaction.setPaymentId(payment.getId());
        transaction.setStatus(TransactionStatus.COMPLETED.name());
        
        // Save transaction and update wallet balance with optimistic locking retry
        return completePaymentWithRetry(walletId, transaction, normalizedAmount);
    }
    
    private Transaction completePaymentWithRetry(String walletId, Transaction transaction, BigDecimal amount) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                // Get fresh wallet data
                Wallet wallet = walletService.getWallet(walletId);
                
                // Set balance information in transaction
                transaction.setRelativeBalance(wallet.getBalance());
                BigDecimal newBalance = wallet.getBalance().add(amount);
                transaction.setCurrentBalance(newBalance);
                
                // Update wallet
                wallet.setBalance(newBalance);
                wallet.getTransactions().add(transaction);
                
                walletRepository.save(wallet);
                log.info("Payment completed successfully on attempt {}", attempt + 1);
                return transaction;
                
            } catch (OptimisticLockingFailureException e) {
                attempt++;
                log.warn("Optimistic locking failure on attempt {}, retrying...", attempt);
                
                if (attempt >= MAX_RETRIES) {
                    log.error("Max retries exceeded for payment completion");
                    throw new PaymentFailedException("Unable to complete payment after " + MAX_RETRIES + " attempts due to concurrent updates");
                }
                
                // Exponential backoff with jitter
                long backoffMs = 10 + ThreadLocalRandom.current().nextInt(40) * (1L << Math.min(attempt - 1, 4));
                LockSupport.parkNanos(Duration.ofMillis(backoffMs).toNanos());
            }
        }
        
        throw new PaymentFailedException("Unexpected error in payment completion");
    }
    
    private void saveTransactionWithRetry(String walletId, Transaction transaction) {
        int attempt = 0;
        while (attempt < MAX_RETRIES) {
            try {
                Wallet wallet = walletService.getWallet(walletId);
                wallet.getTransactions().add(transaction);
                walletRepository.save(wallet);
                log.info("Failed transaction saved successfully on attempt {}", attempt + 1);
                return;
                
            } catch (OptimisticLockingFailureException e) {
                attempt++;
                log.warn("Optimistic locking failure saving failed transaction on attempt {}, retrying...", attempt);
                
                if (attempt >= MAX_RETRIES) {
                    log.error("Max retries exceeded for saving failed transaction");
                    return; // We tried our best to save the failed transaction
                }
                
                long backoffMs = 10 + ThreadLocalRandom.current().nextInt(40);
                LockSupport.parkNanos(Duration.ofMillis(backoffMs).toNanos());
            }
        }
    }
}
