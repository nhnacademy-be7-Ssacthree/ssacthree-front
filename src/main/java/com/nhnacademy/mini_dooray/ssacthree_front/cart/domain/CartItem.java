package com.nhnacademy.mini_dooray.ssacthree_front.cart.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("cartItem")
public class CartItem {

    @Id
    private long id; //도서 id

    private String title; //도서 제목

    @Setter
    private int quantity; // 도서 수량

    private double price; // 가격

    private byte[] image; //이미지

}
