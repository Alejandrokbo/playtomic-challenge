package com.playtomic.tests.wallet.entity;

import com.playtomic.tests.wallet.constants.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document("transaction")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private double amount;
    private String status;
    private String concept;
    private double relativeBalance;
    private double currentBalance;
    private String paymentId;
    private String cardAlias;
    private Date transactionDate;

    public Transaction(double amount, String concept, TransactionStatus status) {
        this.amount = amount;
        this.concept = concept;
        this.status = status.name();
    }
}
