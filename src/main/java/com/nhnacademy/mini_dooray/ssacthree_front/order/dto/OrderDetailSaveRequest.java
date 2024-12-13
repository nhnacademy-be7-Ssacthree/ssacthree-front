package com.nhnacademy.mini_dooray.ssacthree_front.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderDetailSaveRequest {

    private Long bookId;
    private Long orderId;
    private Long couponId;
    private Integer quantity;
    private Integer bookPriceAtOrder;
    @Setter
    private Long packagingId;
}
