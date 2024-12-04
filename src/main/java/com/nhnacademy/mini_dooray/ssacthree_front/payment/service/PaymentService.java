package com.nhnacademy.mini_dooray.ssacthree_front.payment.service;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.dto.PaymentRequest;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    MessageResponse savePayment(PaymentRequest paymentRequest);
    ResponseEntity<String> cancelPayment(String paymentKey, String cancelReason);
}
