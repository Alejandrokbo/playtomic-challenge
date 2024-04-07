package com.playtomic.tests.wallet.constants;

import lombok.Getter;

@Getter
public enum ResponseConstants {
    /**
     * Custom OK Responses
     */
    OK("OK", "All good."),
    WALLET_CREATED("SUCCESS", "Wallet created successfully."),
    WALLET_FOUND("SUCCESS", "Wallet found successfully."),
    PAYMENT_OK("SUCCESS", "Payment order proceed successfully."),
    CARD_CREATED("SUCCESS", "Card created successfully."),
    TRANSACTIONS("SUCCESS", "Transactions retrieved successfully."),

    /**
     * Custom error for responses
     */
    E400("BAD_REQUEST", "The request is not valid. Check the parameters."),
    E404("NOT_FOUND", "This content requested doesn't exist."),
    E500("GATEWAY_ERROR", "Something was wrong."),
    WALLET_NOT_FOUND("ERROR", "Wallet not found."),
    PAYMENT_FAILED("FAILURE", "Payment failed."),
    AMOUNT_TOO_SMALL("ERROR", "The amount provided is too small, should be more than 10.00."),
    CARD_NOT_FOUND("CARD_NOT_FOUND", "The Card Alias provided doesn't exist."),
    CARD_NOT_VALID("NOT_VALID_CARD", "The credit card is not valid. Check the parameters.");

    private final String status;
    private final String message;

    ResponseConstants(String status, String message) {
        this.status = status;
        this.message = message;
    }
}