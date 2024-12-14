package com.nhnacademy.mini_dooray.ssacthree_front.payment.service;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.adapter.PaymentAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.dto.PaymentCancelRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.dto.PaymentRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceTest {

    @Mock
    private PaymentAdapter paymentAdapter;

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    private PaymentRequest paymentRequest;
    private PaymentCancelRequest paymentCancelRequest;
    private MessageResponse messageResponse;

    @BeforeEach
    void setUp() {
        paymentRequest = new PaymentRequest(1L, 2L, 1000, "DONE", "paymentKey123", "2024-12-11T12:00:00");
        paymentCancelRequest = new PaymentCancelRequest("paymentKey123", "Customer Request");
        messageResponse = new MessageResponse("Payment processed successfully");
    }

    @Test
    void savePayment_success() {
        // Given
        when(paymentAdapter.savePayment(paymentRequest)).thenReturn(ResponseEntity.ok(messageResponse));

        // When
        MessageResponse response = paymentServiceImpl.savePayment(paymentRequest);

        // Then
        assertNotNull(response);
        assertEquals("Payment processed successfully", response.getMessage());
        verify(paymentAdapter, times(1)).savePayment(paymentRequest);
    }

    @Test
    void cancelPayment_success() {
        // Given
        when(paymentAdapter.cancelPayment(1L, paymentCancelRequest)).thenReturn(ResponseEntity.ok(messageResponse));

        // When
        MessageResponse response = paymentServiceImpl.cancelPayment(1L, paymentCancelRequest);

        // Then
        assertNotNull(response);
        assertEquals("Payment processed successfully", response.getMessage());
        verify(paymentAdapter, times(1)).cancelPayment(1L, paymentCancelRequest);
    }

    @Test
    void savePayment_failed() {
        // Given
        MessageResponse errorResponse = new MessageResponse("Payment failed");
        when(paymentAdapter.savePayment(paymentRequest)).thenReturn(ResponseEntity.ok(errorResponse));

        // When
        MessageResponse response = paymentServiceImpl.savePayment(paymentRequest);

        // Then
        assertNotNull(response);
        assertEquals("Payment failed", response.getMessage());
        verify(paymentAdapter, times(1)).savePayment(paymentRequest);
    }

    @Test
    void cancelPayment_failed() {
        // Given
        MessageResponse errorResponse = new MessageResponse("Payment cancellation failed");
        when(paymentAdapter.cancelPayment(1L, paymentCancelRequest)).thenReturn(ResponseEntity.ok(errorResponse));

        // When
        MessageResponse response = paymentServiceImpl.cancelPayment(1L, paymentCancelRequest);

        // Then
        assertNotNull(response);
        assertEquals("Payment cancellation failed", response.getMessage());
        verify(paymentAdapter, times(1)).cancelPayment(1L, paymentCancelRequest);
    }
}
