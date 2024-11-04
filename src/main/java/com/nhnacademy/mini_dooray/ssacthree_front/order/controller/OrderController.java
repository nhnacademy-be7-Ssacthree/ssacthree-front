package com.nhnacademy.mini_dooray.ssacthree_front.order.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/orderSheet")
public class OrderController {

    private OrderService orderService;

    // 1. 비회원, 회원 주문 페이지 이동
    // 장바구니 -> 주문
    // 책 -> 바로 주문

    @GetMapping
    public String OrderSheet() {
        return "/order/ordersheet";
    }

    // 2. 비회원, 회원 주문 구현

    // 3. 비회원, 회원 주문 내역 페이지 구현

}
