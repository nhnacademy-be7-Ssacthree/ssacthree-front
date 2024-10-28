package com.nhnacademy.mini_dooray.ssacthree_front.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //domain은 수정될 예정 /{memberId}/cart
    @GetMapping("/cart")
    public String showShop() {
        return "cart";
    }

    @GetMapping("/cart/{memberId}")
    public String showCart(@PathVariable("memberId") long memberId, Model model) {
        // CartService를 통해 장바구니 항목 조회
        List<CartItem> cartItems = cartService.getCartItemsByUserId(memberId);

        // 장바구니 항목을 모델에 추가
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("memberId", memberId);

        // "cart" 템플릿을 반환
        return "cart";
    }
}