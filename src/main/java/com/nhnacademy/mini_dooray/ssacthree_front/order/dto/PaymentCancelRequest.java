package com.nhnacademy.mini_dooray.ssacthree_front.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PaymentCancelRequest {
    String orderId;
    String paymentId;
}
