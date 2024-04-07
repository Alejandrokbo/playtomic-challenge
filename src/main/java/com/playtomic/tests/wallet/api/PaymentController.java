package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.constants.ResponseConstants;
import com.playtomic.tests.wallet.entity.Transaction;
import com.playtomic.tests.wallet.http.Response;
import com.playtomic.tests.wallet.http.ResponseHandler;
import com.playtomic.tests.wallet.model.PaymentOrderDto;
import com.playtomic.tests.wallet.service.PaymentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/payment")
@EnableAsync
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ModelMapper modelMapper;

    @Async
    @PostMapping("/process")
    public CompletableFuture<ResponseEntity<Response>> processPayment(@RequestBody PaymentOrderDto paymentOrder) {
        Transaction transaction = paymentService.processPayment(paymentOrder.getWalletId(), paymentOrder.getAmount(), paymentOrder.getCardAlias());
        return CompletableFuture.completedFuture(ResponseHandler.response(
                ResponseConstants.PAYMENT_OK.getStatus(),
                ResponseConstants.PAYMENT_OK.getMessage(),
                HttpStatus.OK,
                modelMapper.map(transaction, Transaction.class)));
    }

}
