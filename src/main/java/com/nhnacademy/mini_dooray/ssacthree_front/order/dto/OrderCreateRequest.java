package com.nhnacademy.mini_dooray.ssacthree_front.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class OrderCreateRequest {
    // 주문 상품 정보 -

    // 구매자 정보
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;

    // 배송지 정보
    private String recipientName;
    private String recipientPhone;
    private String postalCode;
    private String roadAddress;
    private String detailAddress;
    private LocalDate deliveryDate;

    // 포인트 사용
    private int pointToUse;

    // 총 금액
    private int paymentPrice;


}
