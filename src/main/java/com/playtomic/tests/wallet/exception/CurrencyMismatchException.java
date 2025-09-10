package com.playtomic.tests.wallet.exception;

public class CurrencyMismatchException extends RuntimeException {
    public CurrencyMismatchException() {
        super("Card currency does not match wallet currency");
    }

    public CurrencyMismatchException(String message) {
        super(message);
    }
}
