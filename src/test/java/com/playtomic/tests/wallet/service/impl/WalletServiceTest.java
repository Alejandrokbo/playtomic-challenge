package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.WalletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WalletServiceTest {

    @InjectMocks
    WalletService walletService;

    @Mock
    WalletRepository walletRepository;

    private static final String WALLET_ID = "6611244f0f9a896c79c3608f";


    @Test
    public void createWalletTest() {
        String playerName = "Alejandro";
        Wallet expectedWallet = new Wallet(playerName, BigDecimal.valueOf(0.00), "EUR");

        when(walletRepository.save(any(Wallet.class))).thenReturn(expectedWallet);

        Wallet createdWallet = walletService.createWallet(playerName);
        assertEquals(expectedWallet.getPlayerName(), createdWallet.getPlayerName());
        assertEquals(expectedWallet.getCurrency(), createdWallet.getCurrency());
    }

    @Test
    public void getWalletTest() {
        Wallet wallet = new Wallet("Alejandro", BigDecimal.valueOf(0.00), "EUR");

        when(walletRepository.findById(WALLET_ID)).thenReturn(Optional.of(wallet));

        Wallet retrievedWallet = walletService.getWallet(WALLET_ID);
        assertEquals(wallet, retrievedWallet);
    }

    @Test
    public void getWalletNotFoundTest() {
        when(walletRepository.findById(WALLET_ID)).thenThrow(WalletNotFoundException.class);
        assertThrows(WalletNotFoundException.class, () -> walletRepository.findById(WALLET_ID));
    }

    @Test
    void rechargeWalletTest() {

    }
}
