package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.constants.ResponseConstants;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.http.Response;
import com.playtomic.tests.wallet.http.ResponseHandler;
import com.playtomic.tests.wallet.model.CardDto;
import com.playtomic.tests.wallet.model.WalletDto;
import com.playtomic.tests.wallet.service.TransactionService;
import com.playtomic.tests.wallet.service.WalletService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {
    private final Logger log = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    private WalletService walletService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ModelMapper modelMapper;

    @RequestMapping("/")
    void log() {
        log.info("Logging from /");
    }

    @PostMapping("/add/{playerName}")
    public ResponseEntity<Response> addWallet(@PathVariable String playerName) {
        log.info("Adding wallet for player: " + playerName);
        return ResponseHandler.response(
                ResponseConstants.WALLET_CREATED.getStatus(),
                ResponseConstants.WALLET_CREATED.getMessage(),
                HttpStatus.OK,
                modelMapper.map(walletService.createWallet(playerName), WalletDto.class));
    }

    @PostMapping("/add/card")
    public ResponseEntity<Response> addCard(
            @RequestBody CardDto card) throws WalletNotFoundException {
        log.info("Adding card to wallet with id: " + card.getWalletId());
        Wallet wallet = walletService.addNewCard(card);

        return ResponseHandler.response(
                ResponseConstants.CARD_CREATED.getStatus(),
                ResponseConstants.CARD_CREATED.getMessage(),
                HttpStatus.OK,
                modelMapper.map(wallet, WalletDto.class));
    }

    @GetMapping("{id}")
    ResponseEntity<Response> getWallet(
            @PathVariable String id) throws WalletNotFoundException {
        log.info("Getting wallet with id: " + id);
        Wallet wallet = walletService.getWallet(id);

        return ResponseHandler.response(
                ResponseConstants.WALLET_FOUND.getStatus(),
                ResponseConstants.WALLET_FOUND.getMessage(),
                HttpStatus.OK,
                modelMapper.map(wallet, WalletDto.class));
    }

    @GetMapping("/transactions/{walletId}/{onlyCompleted}")
    ResponseEntity<Response> getTransactions(
            @PathVariable String walletId,
            @PathVariable boolean onlyCompleted) {
        log.info("Getting transactions for wallet with id: {}", walletId);
        return ResponseHandler.response(
                ResponseConstants.TRANSACTIONS.getStatus(),
                ResponseConstants.TRANSACTIONS.getMessage(),
                HttpStatus.OK,
                transactionService.getTransactions(walletId, onlyCompleted));
    }


}
