package com.playtomic.tests.wallet.repository;

import com.playtomic.tests.wallet.entity.Wallet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
public class WalletRepositoryTest {

    @Autowired
    private WalletRepository walletRepository;

    @Test
    public void createWalletTest() {
        Wallet newWallet = new Wallet("John DOe", BigDecimal.valueOf(0.00));
        walletRepository.save(newWallet);

        Optional<Wallet> foundWallet = walletRepository.findById(newWallet.getId());

        assertThat(foundWallet).isPresent();
        assertThat(foundWallet.get().getPlayerName()).isEqualTo("John DOe");

    }
}
