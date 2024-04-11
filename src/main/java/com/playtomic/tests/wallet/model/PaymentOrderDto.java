package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentOrderDto {
    @JsonProperty("walletId")
    private String walletId;
    @JsonProperty("cardAlias")
    private String cardAlias;
    @JsonProperty("amount")
    private BigDecimal amount;

    public PaymentOrderDto(String walletId, String cardAlias, BigDecimal amount) {
        this.walletId = walletId;
        this.cardAlias = cardAlias;
        this.amount = amount;
    }
}
