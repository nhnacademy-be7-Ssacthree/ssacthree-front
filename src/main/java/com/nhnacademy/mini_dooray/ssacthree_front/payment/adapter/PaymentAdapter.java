package com.nhnacademy.mini_dooray.ssacthree_front.payment.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.dto.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "paymentClient")
public interface PaymentAdapter {

    @PostMapping("/shop/payment")
    ResponseEntity<MessageResponse> savePayment(PaymentRequest paymentRequest);
}
