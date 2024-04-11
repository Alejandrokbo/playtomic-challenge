package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.entity.Card;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.cardException.NotValidCardException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.model.CardDto;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.utils.CardValidations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.playtomic.tests.wallet.utils.CardValidations.formatCreditCardNumber;

@Service
public class WalletService {

    @Autowired
    private WalletRepository walletRepository;

    public Wallet createWallet(String playerName) {
        Wallet newWallet = new Wallet(playerName, BigDecimal.valueOf(0.00));
        return walletRepository.save(newWallet);
    }

    public Wallet getWallet(String id) {
        return walletRepository.findById(id).orElseThrow(WalletNotFoundException::new);
    }

    public Wallet addNewCard(CardDto card) {

        if (!CardValidations.isValidCard(card.getPan(), card.getCvv(), card.getExpirationDate())) {
            throw new NotValidCardException();
        }

        Wallet wallet = getWallet(card.getWalletId());

        Card newCard = new Card(
                formatCreditCardNumber(card.getPan()),
                card.getCvv(),
                card.getAlias(),
                card.getHolder(),
                card.getCurrency(),
                card.getExpirationDate()
        );
        List<Card> cards = wallet.getCards();
        cards.add(newCard);
        wallet.setCards(cards);
        return walletRepository.save(wallet);
    }

}
