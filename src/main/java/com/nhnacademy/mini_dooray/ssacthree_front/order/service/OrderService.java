package com.nhnacademy.mini_dooray.ssacthree_front.order.service;

import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderFormRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderSaveRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;

public interface OrderService {
    OrderResponse createOrder(OrderSaveRequest orderSaveRequest);

    void prepareOrderCart(HttpServletRequest request, Model model);

    void prepareOrderNow(HttpServletRequest request, Long bookId, int quantity, Model model);

    void processOrder(String memberId, Integer paymentPrice, OrderFormRequest orderFormRequest, HttpSession session, Model model);
}
