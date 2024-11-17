package com.nhnacademy.mini_dooray.ssacthree_front.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class OrderFormRequest {
    //ordernum

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
    private String orderRequest;
    private LocalDate deliveryDate;

    // 포인트 사용
    // TODO : null가능하게 !
    private Integer pointToUse;

    // 적립 포인트

    // 순수 금액

    // 총 주문 금액
    private Integer paymentPrice;

    // 고객 정보 customerinfo -id
    private Long customerId;

    // 쿠폰 정보

    // 주문 도서 정보 list


}
