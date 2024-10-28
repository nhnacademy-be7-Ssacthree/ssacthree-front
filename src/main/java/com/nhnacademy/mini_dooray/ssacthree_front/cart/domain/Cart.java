package com.nhnacademy.mini_dooray.ssacthree_front.cart.domain;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Data
@NoArgsConstructor
@RedisHash("cart")
public class Cart {

    @Id
    private long Id; //장바구니 ID 레디스에서 쓸 id

    @Indexed
    private long userId; //사용자 ID (고객 id)

    private List<CartItem> cartItems; //장바구니 아이템 목록

    private int totalPrice; // 총 가격


}
