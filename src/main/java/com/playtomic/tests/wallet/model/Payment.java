package com.playtomic.tests.wallet.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

@Data
public class Payment {

    @NonNull
    private String id;

    @JsonCreator
    public Payment(@JsonProperty(value = "id", required = true) @NotNull String id) {
        this.id = id;
    }
}
