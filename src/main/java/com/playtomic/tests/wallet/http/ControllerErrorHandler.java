package com.playtomic.tests.wallet.http;

import com.playtomic.tests.wallet.constants.ResponseConstants;
import com.playtomic.tests.wallet.exception.CurrencyMismatchException;
import com.playtomic.tests.wallet.exception.InvalidAmountException;
import com.playtomic.tests.wallet.exception.PaymentFailedException;
import com.playtomic.tests.wallet.exception.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.exception.cardException.NotCardFoundException;
import com.playtomic.tests.wallet.exception.cardException.NotValidCardException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerErrorHandler {
    private final Logger log = LoggerFactory.getLogger(ControllerErrorHandler.class);

    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response> handleWalletNotFoundException(WalletNotFoundException e) {
        log.error("Wallet not found: Reason: {}", e.getMessage());
        return ResponseHandler.response(
                ResponseConstants.WALLET_NOT_FOUND.getStatus(),
                ResponseConstants.WALLET_NOT_FOUND.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NotValidCardException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleNotValidCreditCard(NotValidCardException e) {
        log.error("The credit card is not valid, Reason: {}", e.getMessage());
        return ResponseHandler.response(
                ResponseConstants.CARD_NOT_VALID.getStatus(),
                ResponseConstants.CARD_NOT_VALID.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotCardFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Response> handleCardNotFount(NotCardFoundException e) {
        log.error("Card not found: {}", e.getMessage());
        return ResponseHandler.response(
                ResponseConstants.CARD_NOT_FOUND.getStatus(),
                ResponseConstants.CARD_NOT_FOUND.getMessage(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(StripeAmountTooSmallException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<Response> handleAmountToSmall(StripeAmountTooSmallException e) {
        log.error("Payment fail, due small amount: {}", e.getMessage());
        return ResponseHandler.response(
                ResponseConstants.AMOUNT_TOO_SMALL.getStatus(),
                ResponseConstants.AMOUNT_TOO_SMALL.getMessage(),
                HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(PaymentFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Response> handleFailedPayment(PaymentFailedException e) {
        log.error("Payment fail, reason: {}", e.getMessage());
        return ResponseHandler.response(
                ResponseConstants.PAYMENT_FAILED.getStatus(),
                ResponseConstants.PAYMENT_FAILED.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @ExceptionHandler(InvalidAmountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleInvalidAmount(InvalidAmountException e) {
        log.error("Invalid amount provided: {}", e.getMessage());
        return ResponseHandler.response(
                "INVALID_AMOUNT",
                e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(CurrencyMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Response> handleCurrencyMismatch(CurrencyMismatchException e) {
        log.error("Currency mismatch: {}", e.getMessage());
        return ResponseHandler.response(
                "CURRENCY_MISMATCH",
                e.getMessage(),
                HttpStatus.BAD_REQUEST);
    }
}
