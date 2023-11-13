package com.java.novice.OrderService.external.decoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.novice.OrderService.external.response.ErrorResponse;
import com.java.novice.OrderService.exception.CustomException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

@Log4j2
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {

        ObjectMapper objectMapper = new ObjectMapper();
        log.info("::{}", response.request().url());
        log.info("::{}", response.request().headers());
        ErrorResponse errorResponse;
        try {
            errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);
            return new CustomException(errorResponse.getErrorMessage(), errorResponse.getErrorCode(), response.status());
        } catch (IOException e) {
            throw new CustomException("Internal Server Error", "INTERNAL_SERVER_ERROR", 500);
        }

    }
}
