package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.constants.TransactionStatus;
import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private WalletService walletService;

    public List<Transaction> getTransactions(String walletId, boolean onlyCompleted) {
        Wallet wallet = walletService.getWallet(walletId);
        List<Transaction> transactions = wallet.getTransactions();
        if (onlyCompleted) {
            transactions.removeIf(transaction -> transaction.getStatus().equals(TransactionStatus.FAILED.name()));
        }
        return transactions;
    }
}
