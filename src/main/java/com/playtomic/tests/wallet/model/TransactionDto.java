package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionDto {
    @JsonProperty("amount")
    private double amount;
    @JsonProperty("status")
    private String status;
    @JsonProperty("concept")
    private String concept;
    @JsonProperty("paymentId")
    private String paymentId;
    @JsonProperty("cardAlias")
    private String cardAlias;
    @JsonProperty("transactionDate")
    private Date transactionDate;
    @JsonProperty("relativeBalance")
    private BigDecimal relativeBalance;
    @JsonProperty("currentBalance")
    private BigDecimal currentBalance;
}
