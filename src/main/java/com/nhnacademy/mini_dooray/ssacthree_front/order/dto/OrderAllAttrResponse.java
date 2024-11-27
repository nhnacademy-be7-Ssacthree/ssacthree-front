package com.nhnacademy.mini_dooray.ssacthree_front.order.dto;

import java.time.LocalDate;

public class OrderDetailResponse {



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

  private Integer pointToSave;

  // 적립 포인트

  // 순수 금액

  // 총 주문 금액
  private Integer paymentPrice;

  // 고객 정보 customerinfo -id
  private Long customerId;

  // 쿠폰 정보

  // 주문 도서 정보 list
  private String orderNumber;

}
