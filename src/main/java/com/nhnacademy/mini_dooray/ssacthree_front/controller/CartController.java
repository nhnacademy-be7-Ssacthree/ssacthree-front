package com.nhnacademy.mini_dooray.ssacthree_front.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/shop")
    public String viewCart(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        List<CartItem> cartItems = cartService.initializeCart(session); // 서비스에서 장바구니 초기화

        System.out.println("장바구니 아이템 수: " + cartItems.size());
        for (CartItem item : cartItems) {
            System.out.println("아이템 ID: " + item.getId() + ", 제목: " + item.getTitle() + ", 수량: " + item.getQuantity());
        }

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cartItems));
        return "cart";
    }

    @PostMapping("shop/save")
    @ResponseBody
    public ResponseEntity<String> saveCart(HttpServletRequest request, @RequestBody List<CartItem> cartItems) {
        HttpSession session = request.getSession();
        cartService.saveCart(session, cartItems); // 서비스에 세션과 장바구니 항목을 전달하여 저장

        return ResponseEntity.ok("Cart saved successfully");
    }

    private String generateCartId() {
        return "guestCart:" + System.currentTimeMillis(); //비회원 장바구니
    }
}