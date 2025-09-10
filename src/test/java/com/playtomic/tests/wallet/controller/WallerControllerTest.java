package com.playtomic.tests.wallet.controller;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.http.Response;
import com.playtomic.tests.wallet.model.CardDto;
import com.playtomic.tests.wallet.repository.WalletRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WallerControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WalletRepository walletRepository;

    private static final String WALLET_ID = "661145e359424c661e72d4a4";

    @BeforeEach
    void setup() {
        Wallet wallet = new Wallet("John Doe", BigDecimal.valueOf(50.00));
        wallet.setId(WALLET_ID);
        walletRepository.save(wallet);
    }

    @Test
    void add_wallet_test() {
        String name = "John Doe";
        assertThat(this.restTemplate.postForObject(String.format("http://localhost:%d/wallet/add/%s", port, name), null,
                Response.class).data.toString()).contains("John Doe");
    }

    @Test
    void add_card_test() {
        String name = "John Doe";

        CardDto card = new CardDto();
        card.setPan("4300558918745056");
        card.setCvv(123);
        card.setAlias("My Card");
        card.setHolder(name);
        card.setCurrency("EUR");
        card.setExpirationDate("12/23");
        card.setWalletId("661145e359424c661e72d4a4");

        assertThat(this.restTemplate.postForObject(String.format("http://localhost:%d/wallet/add/card", port), card,
                Response.class).data.toString()).contains(name).contains("4300 5589 1874 5056");
    }

    @Test
    void get_wallet_from_id_test() {
        assertThat(this.restTemplate.getForObject(String.format("http://localhost:%d/wallet/%s", port, WALLET_ID),
                Response.class).data.toString()).contains("John Doe").contains("50.0");
    }
}
