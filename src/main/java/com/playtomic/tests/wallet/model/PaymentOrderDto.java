package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentOrderDto {
    @JsonProperty("walletId")
    private String walletId;
    @JsonProperty("cardAlias")
    private String cardAlias;
    @JsonProperty("amount")
    private double amount;

    public PaymentOrderDto(String walletId, String cardAlias, double amount) {
        this.walletId = walletId;
        this.cardAlias = cardAlias;
        this.amount = amount;
    }
}
