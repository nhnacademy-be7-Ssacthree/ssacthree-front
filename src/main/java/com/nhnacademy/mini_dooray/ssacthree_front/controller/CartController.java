package com.nhnacademy.mini_dooray.ssacthree_front.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.aop.annotation.LoginRequired;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private static final String CART_REDIRECT = "redirect:/shop";


    @GetMapping("/shop")
    public String viewCart(HttpServletRequest request, Model model) {
        List<CartItem> cartItems = cartService.initializeCart(request); // 서비스에서 장바구니 초기화
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cartItems));
        return "cart";
    }

    @PostMapping("/shop/")
    public String addNewBook(HttpSession session, @RequestParam Long itemId,
                                                  @RequestParam String title,
                                                  @RequestParam int price,
                                                  @RequestParam String image) { //새로운 책 장바구니에 추가
        cartService.addNewBook(session,itemId,title,price,image);
        return CART_REDIRECT;
    }

    @PutMapping("/shop/{itemId}")
    public String changeQuantity(HttpSession session, @PathVariable Long itemId, @RequestParam int quantityChange) { //수량 변경
        cartService.updateItemQuantity(session, itemId, quantityChange);
        return CART_REDIRECT; // 변경 후 장바구니 페이지로 리다이렉트
    }


    @DeleteMapping("/shop/{itemId}")
    public String deleteCartItem(HttpSession session, @PathVariable Long itemId) {
        cartService.deleteItem(session,itemId);
        return CART_REDIRECT;
    }


}