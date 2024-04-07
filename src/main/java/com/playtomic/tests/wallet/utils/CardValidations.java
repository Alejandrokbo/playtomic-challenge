package com.playtomic.tests.wallet.utils;

public class CardValidations {

    public static String formatCreditCardNumber(String cardNumber) {
        String cleanedNumber = cardNumber.replaceAll("[^0-9]", "");
        return cleanedNumber.replaceAll("\\d{4}", "$0 ").trim();
    }

    public static boolean isValidCard(String cardNumber, int cvv, String expirationDate) {
        return isValidPan(cardNumber) && isValidCvv(cvv) && isValidExpirationDate(expirationDate);
    }

    private static boolean isValidExpirationDate(String expirationDate) {
        return expirationDate != null && expirationDate.matches("^(0[1-9]|1[0-2])/[0-9]{2}$");
    }

    private static boolean isValidCvv(int cvv) {
        String strCvv = String.valueOf(cvv);
        return strCvv.matches("\\d+") && strCvv.length() == 3;
    }

    /**
     * Using Luhn algorithm we can check if is a valid credit card
     */
    private static boolean isValidPan(String cardNumber) {
        String cleanedNumber = cardNumber.replaceAll("[^0-9]", "");

        int[] digits = new int[cleanedNumber.length()];
        for (int i = 0; i < cleanedNumber.length(); i++) {
            digits[i] = Integer.parseInt(cleanedNumber.substring(i, i + 1));
        }

        for (int i = digits.length - 2; i >= 0; i -= 2) {
            int doubledDigit = digits[i] * 2;
            if (doubledDigit > 9) {
                doubledDigit -= 9;
            }
            digits[i] = doubledDigit;
        }

        int sum = 0;
        for (int digit : digits) {
            sum += digit;
        }

        return sum % 10 == 0;
    }

}
