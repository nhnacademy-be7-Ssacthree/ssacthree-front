package com.nhnacademy.mini_dooray.ssacthree_front.commons.util;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import static org.junit.jupiter.api.Assertions.*;

class ResponseEntityHandlerTest {

    @Test
    void testGetResponseBody_Success() {
        // Given
        String expectedBody = "Success";
        ResponseEntity<String> response = ResponseEntity.ok(expectedBody);

        // When
        String result = ResponseEntityHandler.getResponseBody(
            response,
            () -> new RuntimeException("Should not be thrown")
        );

        // Then
        assertEquals(expectedBody, result);
    }

    @Test
    void testGetResponseBody_NonSuccessfulStatus() {
        // Given
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> ResponseEntityHandler.getResponseBody(
                response,
                () -> new RuntimeException("Request failed")
            )
        );

        assertEquals("Request failed", exception.getMessage());
    }

    @Test
    void testGetResponseBody_WithStringMessage() {
        // Given
        String expectedBody = "Success";
        ResponseEntity<String> response = ResponseEntity.ok(expectedBody);

        // When
        String result = ResponseEntityHandler.getResponseBody(response, "Should not be thrown");

        // Then
        assertEquals(expectedBody, result);
    }

    @Test
    void testGetResponseBody_WithStringMessage_Failure() {
        // Given
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);

        // When & Then
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> ResponseEntityHandler.getResponseBody(response, "Server error occurred")
        );

        assertEquals("Server error occurred", exception.getMessage());
    }

    @Test
    void testGetResponseBody_HandlesHttpClientErrorException() {
        // This test demonstrates the pattern used in service implementations
        // where HttpClientErrorException would be caught and re-thrown as custom exception
        
        // Given
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        // When & Then
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> ResponseEntityHandler.getResponseBody(
                response,
                () -> new IllegalStateException("Resource not found")
            )
        );

        assertEquals("Resource not found", exception.getMessage());
    }

    @Test
    void testGetResponseBody_CreatedStatus() {
        // Given
        String expectedBody = "Created";
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.CREATED).body(expectedBody);

        // When
        String result = ResponseEntityHandler.getResponseBody(
            response,
            () -> new RuntimeException("Should not be thrown")
        );

        // Then
        assertEquals(expectedBody, result);
    }

    @Test
    void testGetResponseBody_AcceptedStatus() {
        // Given
        String expectedBody = "Accepted";
        ResponseEntity<String> response = ResponseEntity.status(HttpStatus.ACCEPTED).body(expectedBody);

        // When
        String result = ResponseEntityHandler.getResponseBody(
            response,
            () -> new RuntimeException("Should not be thrown")
        );

        // Then
        assertEquals(expectedBody, result);
    }
}
