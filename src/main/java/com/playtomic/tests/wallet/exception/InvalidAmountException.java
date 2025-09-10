package com.playtomic.tests.wallet.exception;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Amount must be positive and have at most 2 decimal places");
    }

    public InvalidAmountException(String message) {
        super(message);
    }
}
