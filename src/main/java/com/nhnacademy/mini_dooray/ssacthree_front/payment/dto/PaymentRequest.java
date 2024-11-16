package com.nhnacademy.mini_dooray.ssacthree_front.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentRequest {

    // 결제의 키 값, 최대 200자, 결제 식별
    String paymentKey;
    // 주문번호, 주문한 결제를 식별, 영문 대소문자, 숫자, 특수문자 -, _로 이루어진 6자 이상 64자 이하의 문자열
    // 결제 데이터 관리를 위해 반드시 저장
    // 20자, 년웡일+임의의값(랜덤)
    String orderId;
    int amount;

}
