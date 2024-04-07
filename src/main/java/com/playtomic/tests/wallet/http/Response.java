package com.playtomic.tests.wallet.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@Data
@JsonPropertyOrder({"status", "message", "data"})
public class Response {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("data")
    public Object data;
    @JsonProperty("status")
    public String statusMessage;
    @JsonProperty("message")
    public String message;
}
