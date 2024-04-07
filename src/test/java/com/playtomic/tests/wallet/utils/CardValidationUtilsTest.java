package com.playtomic.tests.wallet.utils;

import org.junit.jupiter.api.Test;

import static com.playtomic.tests.wallet.utils.CardValidations.isValidCard;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardValidationUtilsTest {

    @Test
    public void giver_a_valid_card_return_ok_test() {
        String pan = "4467985051694";
        int cvv = 123;
        String expirationDate = "12/23";

        assertTrue(isValidCard(pan, cvv, expirationDate));
    }

    @Test
    public void giver_a_invalid_card_return_Fail_test() {
        String pan = "4467985051694";
        int cvv = 1234;
        String expirationDate = "12/2023";

        assertFalse(isValidCard(pan, cvv, expirationDate));
    }

}
