package com.nhnacademy.mini_dooray.ssacthree_front.payment.service;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.dto.PaymentCancelRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.dto.PaymentRequest;

public interface PaymentService {
    MessageResponse savePayment(PaymentRequest paymentRequest);

    MessageResponse cancelPayment(Long orderId, PaymentCancelRequest paymentCancelRequest);
}
