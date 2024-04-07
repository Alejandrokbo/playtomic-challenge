package com.playtomic.tests.wallet.http;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

    @NotNull
    public static ResponseEntity<Response> response(String statusMessage, String message, HttpStatus status) {
        var resp = new Response();
        resp.message = message;
        resp.statusMessage = statusMessage;
        return new ResponseEntity<>(resp, defaultHeaders(), status);
    }

    @NotNull
    public static ResponseEntity<Response> response(String statusMessage, String message, HttpStatus status, Object responseObj) {
        var resp = new Response();
        resp.data = responseObj;
        resp.message = message;
        resp.statusMessage = statusMessage;
        return new ResponseEntity<>(resp, defaultHeaders(), status);
    }

    @NotNull
    private static HttpHeaders defaultHeaders() {
        var headers = new HttpHeaders();
        headers.set("Content-Type", String.valueOf(MediaType.APPLICATION_JSON));
        return headers;
    }

}
