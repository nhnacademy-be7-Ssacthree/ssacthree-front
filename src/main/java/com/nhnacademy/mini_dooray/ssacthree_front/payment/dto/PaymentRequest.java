package com.nhnacademy.mini_dooray.ssacthree_front.payment.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class PaymentRequest {

    private Long orderId;
    private Long method;
    private Integer amount;
    private String status;
    private String paymentKey;
    private String approvedAt;

}
