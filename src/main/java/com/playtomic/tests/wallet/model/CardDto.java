package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("walletId")
    private String walletId;
    @JsonProperty("alias")
    private String alias;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("pan")
    private String pan;
    @JsonProperty("expirationDate")
    private String expirationDate;
    @JsonProperty("cvv")
    private int cvv;
    @JsonProperty("holder")
    private String holder;
}
