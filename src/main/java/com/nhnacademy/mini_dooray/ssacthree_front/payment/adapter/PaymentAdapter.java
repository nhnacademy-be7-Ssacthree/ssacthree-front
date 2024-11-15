package com.nhnacademy.mini_dooray.ssacthree_front.payment.adapter;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "gateway-service", url = "", contextId = "paymentClient")
public interface PaymentAdapter {


}
