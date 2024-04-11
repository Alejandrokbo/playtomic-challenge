package com.playtomic.tests.wallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Data
@Document("wallet")
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {
    @Id
    private String id;
    private String playerName;
    private BigDecimal balance;

    private List<Card> cards;

    private List<Transaction> transactions;

    public Wallet(String playerName, BigDecimal balance) {
        this.playerName = playerName;
        this.balance = balance;
        this.cards = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }
}
