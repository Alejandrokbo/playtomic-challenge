package com.playtomic.tests.wallet.http;


import com.playtomic.tests.wallet.constants.ResponseConstants;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ApiErrorHandler implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<Response> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        @NotNull ResponseEntity<Response> resp = ResponseHandler.response(
                ResponseConstants.OK.getStatus(),
                ResponseConstants.OK.getMessage(),
                HttpStatus.OK
        );

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                resp = ResponseHandler.response(
                        ResponseConstants.E404.getStatus(),
                        ResponseConstants.E404.getMessage(),
                        HttpStatus.NOT_FOUND);
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                resp = ResponseHandler.response(
                        ResponseConstants.E500.getStatus(),
                        ResponseConstants.E500.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            } else if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                resp = ResponseHandler.response(
                        ResponseConstants.E400.getStatus(),
                        ResponseConstants.E400.getMessage(),
                        HttpStatus.BAD_REQUEST);
            } else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                resp = ResponseHandler.response(
                        ResponseConstants.E500.getStatus(),
                        ResponseConstants.E500.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR);

            }
        }
        return resp;
    }

    @Override
    public String getErrorPath() {
        return null;
    }
}
