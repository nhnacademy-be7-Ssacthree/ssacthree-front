package com.nhnacademy.mini_dooray.ssacthree_front.cart.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.dto.CartRequest;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "CartClient")
public interface CartAdapter {

    @GetMapping("/shop")
    ResponseEntity<List<CartItem>> getCartItems(@RequestHeader("Authorization") String authorizationHeader);

    @GetMapping("/shop/{bookId}")
    ResponseEntity<CartItem> getRandomBook(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("bookId") Long bookId);

    @PutMapping("/shop") // 만들어야함
    ResponseEntity<Void> saveCart(@RequestHeader("Authorization") String authorizationHeader, @RequestBody List<CartRequest> cartList);
}
