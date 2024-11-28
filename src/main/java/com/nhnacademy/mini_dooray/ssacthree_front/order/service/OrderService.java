package com.nhnacademy.mini_dooray.ssacthree_front.order.service;

import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderDetailResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.*;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponseWithCount;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderFormRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderSaveRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

public interface OrderService {
    OrderResponse createOrder(OrderSaveRequest orderSaveRequest);

    void prepareOrderCart(HttpServletRequest request, Model model);

    void prepareOrderNow(HttpServletRequest request, Long bookId, int quantity, Model model);

    void processOrder(String memberId, Integer paymentPrice, OrderFormRequest orderFormRequest, HttpSession session, Model model);

    void saveOrder(OrderSaveRequest orderSaveRequest);


    // 회원 주문 조회
    OrderResponseWithCount getOrdersByMemberAndDate(Long customerId, int page, int size, LocalDate startDate, LocalDate endDate);

    AdminOrderResponseWithCount adminGetAllOrders(int page, int size, LocalDate startDate, LocalDate endDate);


    // 주문 상세 조회 (회원, 멤버)
    OrderDetailResponse getOrderDetail(Long orderId);
    OrderDetailResponse getOrderDetailByOrderNumber(String orderNumber, String phoneNumber);
}
