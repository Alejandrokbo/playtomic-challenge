package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.playtomic.tests.wallet.entity.Card;
import com.playtomic.tests.wallet.entity.Transaction;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WalletDto {
    @JsonProperty("id")
    private String id;

    @JsonProperty("playerName")
    private String playerName;

    @JsonProperty("balance")
    private double balance;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("cards")
    private List<Card> cards;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("transactions")
    private List<Transaction> transactions;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Cards {
        private String alias;
        private String pan;
        private String expirationDate;
        private String cvv;
        private String holder;
    }
}
