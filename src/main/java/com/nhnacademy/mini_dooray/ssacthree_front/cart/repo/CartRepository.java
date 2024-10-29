package com.nhnacademy.mini_dooray.ssacthree_front.cart.repo;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.Cart;
import org.springframework.data.repository.CrudRepository;

public interface CartRepository extends CrudRepository<Cart, String> {
    Cart findByUserId(long userId);
}
