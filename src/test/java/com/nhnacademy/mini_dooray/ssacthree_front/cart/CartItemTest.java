package com.nhnacademy.mini_dooray.ssacthree_front.cart;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class CartItemTest {

    @Test
    void testCartItemCreation() {
        long id = 1L;
        String title = "도서 제목";
        int quantity = 2;
        int price = 25000;
        byte[] image = new byte[]{1, 2, 3}; // 임시 이미지 데이터

        CartItem cartItem = new CartItem(id, title, quantity, price, image);

        assertThat(cartItem.getId()).isEqualTo(id);
        assertThat(cartItem.getTitle()).isEqualTo(title);
        assertThat(cartItem.getQuantity()).isEqualTo(quantity);
        assertThat(cartItem.getPrice()).isEqualTo(price);
        assertThat(cartItem.getImage()).isEqualTo(image);
    }

    @Test
    void testCartItemDefaultConstructor() {
        CartItem cartItem = new CartItem();

        assertThat(cartItem.getId()).isEqualTo(0L);
        assertThat(cartItem.getTitle()).isNull();
        assertThat(cartItem.getQuantity()).isEqualTo(0);
        assertThat(cartItem.getPrice()).isEqualTo(0);
        assertThat(cartItem.getImage()).isNull();
    }

    @Test
    void testToString() {
        CartItem cartItem = new CartItem(1L, "도서 제목", 1, 25000, null);

        String expected = "CartItem(id=1, title=도서 제목, quantity=1, price=25000, image=null)";
        assertThat(cartItem.toString()).isEqualTo(expected);
    }
}