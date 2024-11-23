package com.nhnacademy.mini_dooray.ssacthree_front.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderDetailSaveReqeust {

    private Long bookId;
    private Long orderId;
    private Long couponId;
    private Integer quantity;
    private Integer bookPriceAtOrder;
}
