package com.nhnacademy.mini_dooray.ssacthree_front.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CartRequest {

    private Long bookId;
    private int quantity;

}
