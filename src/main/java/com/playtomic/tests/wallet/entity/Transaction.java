package com.playtomic.tests.wallet.entity;

import com.playtomic.tests.wallet.constants.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Document("transaction")
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private BigDecimal amount;
    private String status;
    private String concept;
    private BigDecimal relativeBalance;
    private BigDecimal currentBalance;
    private String paymentId;
    private String cardAlias;
    private Date transactionDate;

    public Transaction(BigDecimal amount, String concept, TransactionStatus status) {
        this.amount = amount;
        this.concept = concept;
        this.status = status.name();
    }
}
