package com.nhnacademy.mini_dooray.ssacthree_front.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderSaveRequest {
    // 주문 완료시 보내야하는 모든 정보들
    // 입력받은 정보, 책 정보들 orderDetail에 필요한, 포인트와 쿠폰 정보,


    // 주문 상품 정보 -
    private List<OrderDetailSaveRequest> orderDetailList;

    private Long customerId;

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
    private Integer pointToUse;

    // 적립 포인트

    // 순수 금액

    // 쿠폰아이디? -> 일단 막아두고 도서에서 쓰게 함.
    // private Long couponId;



}
