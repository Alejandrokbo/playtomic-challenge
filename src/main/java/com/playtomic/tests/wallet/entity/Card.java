package com.playtomic.tests.wallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("card")
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String pan;
    private Integer cvv;
    private String alias;
    private String holder;
    private String currency;
    private String expirationDate;

    public Card(String pan, int cvv, String alias, String holder, String currency, String expirationDate) {
        this.pan = pan;
        this.cvv = cvv;
        this.alias = alias;
        this.holder = holder;
        this.currency = currency;
        this.expirationDate = expirationDate;
    }
}
