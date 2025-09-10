package com.playtomic.tests.wallet.utils;

import com.playtomic.tests.wallet.exception.InvalidAmountException;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MoneyUtils {
    
    private static final int MONETARY_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;
    
    /**
     * Validates and normalizes a monetary amount.
     * 
     * @param amount the amount to validate and normalize
     * @return the normalized amount with proper scale
     * @throws InvalidAmountException if amount is null, negative, or has more than 2 decimal places
     */
    public static BigDecimal validateAndNormalizeAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidAmountException("Amount cannot be null");
        }
        
        if (amount.signum() < 0) {
            throw new InvalidAmountException("Amount cannot be negative");
        }
        
        // Check if amount has more than 2 decimal places
        if (amount.scale() > MONETARY_SCALE) {
            // Check if the extra decimals are actually non-zero
            BigDecimal truncated = amount.setScale(MONETARY_SCALE, RoundingMode.DOWN);
            if (amount.compareTo(truncated) != 0) {
                throw new InvalidAmountException("Amount cannot have more than 2 decimal places");
            }
        }
        
        return amount.setScale(MONETARY_SCALE, ROUNDING_MODE);
    }
    
    /**
     * Validates and normalizes a transaction amount (must be positive, not zero).
     * 
     * @param amount the amount to validate and normalize
     * @return the normalized amount with proper scale
     * @throws InvalidAmountException if amount is null, negative, zero, or has more than 2 decimal places
     */
    public static BigDecimal validateAndNormalizeTransactionAmount(BigDecimal amount) {
        if (amount == null) {
            throw new InvalidAmountException("Amount cannot be null");
        }
        
        if (amount.signum() <= 0) {
            throw new InvalidAmountException("Transaction amount must be positive");
        }
        
        // Check if amount has more than 2 decimal places
        if (amount.scale() > MONETARY_SCALE) {
            // Check if the extra decimals are actually non-zero
            BigDecimal truncated = amount.setScale(MONETARY_SCALE, RoundingMode.DOWN);
            if (amount.compareTo(truncated) != 0) {
                throw new InvalidAmountException("Amount cannot have more than 2 decimal places");
            }
        }
        
        return amount.setScale(MONETARY_SCALE, ROUNDING_MODE);
    }
    
    /**
     * Validates a currency code.
     * 
     * @param currency the currency code to validate
     * @throws InvalidAmountException if currency is null or empty
     */
    public static void validateCurrency(String currency) {
        if (currency == null || currency.trim().isEmpty()) {
            throw new InvalidAmountException("Currency cannot be null or empty");
        }
        
        // Basic currency code validation (3 letter code)
        if (currency.length() != 3) {
            throw new InvalidAmountException("Currency must be a 3-letter code (e.g., EUR, USD)");
        }
    }
}
