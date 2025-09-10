package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.constants.TransactionConcept;
import com.playtomic.tests.wallet.constants.TransactionStatus;
import com.playtomic.tests.wallet.entity.Card;
import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.PaymentFailedException;
import com.playtomic.tests.wallet.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.model.Payment;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.PaymentService;
import com.playtomic.tests.wallet.service.StripeService;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private WalletService walletService;

    @Mock
    private StripeService stripe;

    @Test
    public void processPaymentTest() {
        // Given
        Wallet wallet = new Wallet("Alejandro", BigDecimal.valueOf(100.00));

        Card card = new Card();
        card.setAlias("1");
        card.setPan("1234567890123456");
        card.setCvv(123);
        card.setHolder("Alejandro");
        card.setCurrency("EUR");
        card.setExpirationDate("12/30");

        List<Card> cards = new ArrayList<>();
        cards.add(card);
        wallet.setCards(cards);

        Transaction pendingTransaction = new Transaction(BigDecimal.valueOf(15.00), TransactionConcept.DEPOSIT.name(), TransactionStatus.PENDING);

        // When
        doReturn(wallet).when(walletService).getWallet(wallet.getId()); // Mocking walletService.getWallet method
        doReturn(new Payment("1")).when(stripe).charge(eq(card.getPan()), any(BigDecimal.class)); // Mocking stripe.charge method
        paymentService.processPayment(wallet.getId(), pendingTransaction.getAmount(), "1");

        // Then
        verify(walletRepository, times(1)).save(wallet);
    }

    @Test
    public void processPaymentFailWithTooSmallAmountTest() {
        // Given
        Wallet wallet = new Wallet("Alejandro", BigDecimal.valueOf(100.00));

        Card card = new Card();
        card.setAlias("1");
        card.setPan("1234567890123456");
        card.setCvv(123);
        card.setHolder("Alejandro");
        card.setCurrency("EUR");
        card.setExpirationDate("12/30");

        List<Card> cards = new ArrayList<>();
        cards.add(card);
        wallet.setCards(cards);

        Transaction pendingTransaction = new Transaction(BigDecimal.valueOf(5.00), TransactionConcept.DEPOSIT.name(), TransactionStatus.PENDING);

        // When
        when(walletService.getWallet(wallet.getId())).thenReturn(wallet);
        when(stripe.charge(ArgumentMatchers.anyString(), ArgumentMatchers.any(BigDecimal.class))).thenThrow(new StripeAmountTooSmallException());

        // Then
        assertThrows(StripeAmountTooSmallException.class, () -> paymentService.processPayment(wallet.getId(), pendingTransaction.getAmount(), "1"));
    }

    @Test
    public void processPaymentFailWithPaymentFailedTest() {
        // Given
        Wallet wallet = new Wallet("Alejandro", BigDecimal.valueOf(100.00));

        Card card = new Card();
        card.setAlias("1");
        card.setPan("1234567890123456");
        card.setCvv(123);
        card.setHolder("Alejandro");
        card.setCurrency("EUR");
        card.setExpirationDate("12/30");

        List<Card> cards = new ArrayList<>();
        cards.add(card);
        wallet.setCards(cards);

        Transaction pendingTransaction = new Transaction(BigDecimal.valueOf(5.00), TransactionConcept.DEPOSIT.name(), TransactionStatus.PENDING);

        // When
        when(walletService.getWallet(wallet.getId())).thenReturn(wallet);
        when(stripe.charge(ArgumentMatchers.anyString(), ArgumentMatchers.any(BigDecimal.class))).thenThrow(new PaymentFailedException("Test payment failure"));

        // Then
        assertThrows(PaymentFailedException.class, () -> paymentService.processPayment(wallet.getId(), pendingTransaction.getAmount(), "1"));
    }

}
