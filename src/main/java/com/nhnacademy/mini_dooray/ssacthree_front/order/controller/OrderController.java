package com.nhnacademy.mini_dooray.ssacthree_front.order.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orderSheet")
public class OrderController {

    private OrderServiceImpl orderService;

    private final CartService cartService;


    // 1. 비회원, 회원 주문 페이지 이동 -> 각각 다르게 처리?
    // 장바구니 -> 주문
    // 책 -> 바로 주문

    @GetMapping
    public String OrderSheet(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);

        // 일단 비회원 장바구니부터 처리? 회원일때랑 비회원일때 response다름
        // 이 세션에서 attribute의 cartId 알아낸 다음 redis에서 cartId로 상품 정보 가져오기(아이디랑 수량만 받으면 안되나?)
//        GuestCartInfoResponse guestCartInfoResponse = orderService.createGusetOrderSheet(session);

        List<CartItem> cartItems = cartService.initializeCart(session); // 서비스에서 장바구니 초기화
        // 비회원 장바구니 정보 넣어주기
//        model.addAttribute("guestCartInfo", guestCartInfoResponse);

        // 리스트 형식으로 레디스의 상품 정보들 들어감.
        model.addAttribute("guestCartInfo", cartItems);
        return "order/orderSheet";
    }

    // 2. 비회원, 회원 주문 구현


    // 3. 비회원, 회원 주문 내역 페이지 구현

}
