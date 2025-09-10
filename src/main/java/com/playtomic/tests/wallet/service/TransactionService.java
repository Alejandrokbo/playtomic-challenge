package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.constants.TransactionStatus;
import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.entity.Wallet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private WalletService walletService;

    public List<Transaction> getTransactions(String walletId, boolean onlyCompleted) {
        Wallet wallet = walletService.getWallet(walletId);
        List<Transaction> transactions = wallet.getTransactions();
        
        if (!onlyCompleted) {
            return new ArrayList<>(transactions);
        }
        
        return transactions.stream()
                .filter(transaction -> !TransactionStatus.FAILED.name().equals(transaction.getStatus()))
                .collect(Collectors.toList());
    }
}
