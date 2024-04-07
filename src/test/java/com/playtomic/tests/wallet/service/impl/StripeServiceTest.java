package com.playtomic.tests.wallet.service.impl;


import com.playtomic.tests.wallet.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.exception.StripeServiceException;
import com.playtomic.tests.wallet.service.StripeService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.math.BigDecimal;
import java.net.URI;

/**
 * This test is failing with the current implementation.
 *
 * How would you test this?
 */
public class StripeServiceTest {
    /*
     * This is not the most viable solution but works for the test.
     *
     * The viable solution is load the SpringContext and create the application-test.properties with the url values
     * that we want to test and inject it in a variable through the annotation @Value
     *
     * Example:
     * @Value("${stripe.simulator.charges-uri}") private String chargesUri;
     * @Value("${stripe.simulator.refunds-uri}") private String refundsUri;
     *
     * and then test the charge and refund methods.
     * For some reason the test is not working with the @Value annotation, so I had to use the URI.create method
     *  */
    URI testUri = URI.create("https://sandbox.playtomic.io/v1/stripe-simulator/charges");
    StripeService s = new StripeService(testUri, testUri, new RestTemplateBuilder());

    @Test
    public void test_exception() {
        Assertions.assertThrows(StripeAmountTooSmallException.class, () -> {
            s.charge("4242 4242 4242 4242", new BigDecimal(5));
        });
    }

    @Test
    public void test_ok() throws StripeServiceException {
        s.charge("4242 4242 4242 4242", new BigDecimal(15));
    }
}
