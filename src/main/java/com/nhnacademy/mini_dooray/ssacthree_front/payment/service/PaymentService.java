package com.nhnacademy.mini_dooray.ssacthree_front.payment.service;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.dto.PaymentRequest;

public interface PaymentService {
    public MessageResponse savePayment(PaymentRequest paymentRequest);

    }
