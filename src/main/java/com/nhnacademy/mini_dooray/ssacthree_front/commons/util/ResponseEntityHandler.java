package com.nhnacademy.mini_dooray.ssacthree_front.commons.util;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.function.Supplier;

/**
 * Utility class to handle ResponseEntity responses and eliminate duplicated error handling code.
 * Provides methods to extract response body with consistent error handling.
 */
public class ResponseEntityHandler {

    private ResponseEntityHandler() {
        // Private constructor to prevent instantiation
    }

    /**
     * Extracts the body from a ResponseEntity if the response is successful (2xx status code).
     * Throws the provided exception if the response is not successful or if an HTTP error occurs.
     *
     * @param response          The ResponseEntity to extract the body from
     * @param exceptionSupplier A supplier that provides the exception to throw in case of failure
     * @param <T>               The type of the response body
     * @return The response body
     * @throws RuntimeException The exception provided by the supplier
     */
    public static <T> T getResponseBody(ResponseEntity<T> response, Supplier<RuntimeException> exceptionSupplier) {
        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw exceptionSupplier.get();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Extracts the body from a ResponseEntity if the response is successful (2xx status code).
     * Throws a RuntimeException with the provided error message if the response is not successful.
     *
     * @param response     The ResponseEntity to extract the body from
     * @param errorMessage The error message to use in the exception
     * @param <T>          The type of the response body
     * @return The response body
     * @throws RuntimeException with the provided error message
     */
    public static <T> T getResponseBody(ResponseEntity<T> response, String errorMessage) {
        return getResponseBody(response, () -> new RuntimeException(errorMessage));
    }
}
