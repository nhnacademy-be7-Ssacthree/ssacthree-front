package com.nhnacademy.mini_dooray.ssacthree_front.cart.repo;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import org.springframework.data.repository.CrudRepository;

public interface CartItemRepository extends CrudRepository<CartItem, String> {
}
