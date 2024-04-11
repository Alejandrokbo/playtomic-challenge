package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.constants.TransactionConcept;
import com.playtomic.tests.wallet.constants.TransactionStatus;
import com.playtomic.tests.wallet.entity.Card;
import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.PaymentFailedException;
import com.playtomic.tests.wallet.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.exception.cardException.NotCardFoundException;
import com.playtomic.tests.wallet.model.Payment;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class PaymentService {
    @Autowired
    private WalletService walletService;

    @Autowired
    private StripeService stripe;

    @Autowired
    private WalletRepository walletRepository;

    @Transactional
    public Transaction processPayment(String walletId, BigDecimal amount, String cardAlias) {
        Wallet wallet = walletService.getWallet(walletId);
        Transaction transaction = new Transaction(amount, TransactionConcept.DEPOSIT.name(), TransactionStatus.PENDING);

        List<Transaction> transactions = wallet.getTransactions();
        transactions.add(transaction);
        String cardPayment;
        try {
            cardPayment = wallet.getCards().stream()
                    .filter(card -> card.getAlias().equals(cardAlias))
                    .map(Card::getPan)
                    .findFirst().get();
        } catch (Exception e) {
            throw new NotCardFoundException();
        }
        transaction.setCardAlias(cardAlias);
        Payment paymentId = null;
        try {
            paymentId = stripe.charge(cardPayment, amount);
            Thread.sleep(5000);
        } catch (StripeAmountTooSmallException e) {
            throw new StripeAmountTooSmallException();
        } catch (Exception e) {
            throw new PaymentFailedException();
        } finally {
            if (paymentId == null) {
                transaction.setTransactionDate(new Date());
                transaction.setStatus(TransactionStatus.FAILED.name());
                wallet.setTransactions(transactions);
                walletRepository.save(wallet);
                Thread.currentThread().interrupt();
            }
        }

        transaction.setPaymentId(paymentId != null ? paymentId.getId() : null);
        transaction.setTransactionDate(new Date());
        transaction.setStatus(TransactionStatus.COMPLETED.name());
        transaction.setRelativeBalance(wallet.getBalance());

        BigDecimal currentBalance = wallet.getBalance().add(amount);
        transaction.setCurrentBalance(currentBalance);

        wallet.setBalance(currentBalance);
        wallet.setTransactions(transactions);

        walletRepository.save(wallet);
        return transaction;
    }
}
