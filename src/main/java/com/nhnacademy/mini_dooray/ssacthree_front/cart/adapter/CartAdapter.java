package com.nhnacademy.mini_dooray.ssacthree_front.cart.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.dto.CartRequest;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "CartClient")
public interface CartAdapter {

    @GetMapping("/shop/carts")
    ResponseEntity<List<CartItem>> getCartItems(@RequestHeader("Authorization") String authorizationHeader);

    @GetMapping("/shop/carts/{bookId}")
    ResponseEntity<CartItem> getRandomBook(@RequestHeader("Authorization") String authorizationHeader, @PathVariable("bookId") Long bookId);

    @PostMapping("/shop/carts/cart") // 만들어야함
    ResponseEntity<Void> saveCartInDB(@RequestBody List<CartRequest> cartItems,@RequestParam Long customerId);

    @GetMapping("/shop/members/id")
    ResponseEntity<Long> getCustomerId(@RequestHeader("Authorization") String authorizationHeader);

    @GetMapping("/shop/carts/add")
    ResponseEntity<CartItem> getBook(@RequestParam Long bookId);

    @PostMapping("/shop/carts/logout")
    ResponseEntity<Void> saveCartInDBUseHeader(@RequestHeader("Authorization") String authorizationHeader,@RequestBody List<CartRequest> cartRequests);
}
