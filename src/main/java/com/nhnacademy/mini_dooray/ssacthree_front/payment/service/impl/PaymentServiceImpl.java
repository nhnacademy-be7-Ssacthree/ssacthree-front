package com.nhnacademy.mini_dooray.ssacthree_front.payment.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.adapter.PaymentAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.dto.PaymentCancelRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.dto.PaymentRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentAdapter paymentAdapter;

    // TODO : 결제 정보 저장하기, api요청
    public MessageResponse savePayment(PaymentRequest paymentRequest) {
        ResponseEntity<MessageResponse> response = paymentAdapter.savePayment(paymentRequest);
        return response.getBody();
    }

    public MessageResponse cancelPayment(Long orderId, PaymentCancelRequest paymentCancelRequest) {
        ResponseEntity<MessageResponse> response= paymentAdapter.cancelPayment(orderId, paymentCancelRequest);
        return response.getBody();
    }
}
