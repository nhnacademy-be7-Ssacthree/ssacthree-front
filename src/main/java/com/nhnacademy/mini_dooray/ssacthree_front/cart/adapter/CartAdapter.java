package com.nhnacademy.mini_dooray.ssacthree_front.cart.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "memberClient")
public interface CartAdapter {

    @GetMapping("/shop")
    ResponseEntity<List<CartItem>> getCartItems(@RequestHeader("Authorization") String authorizationHeader);
}
