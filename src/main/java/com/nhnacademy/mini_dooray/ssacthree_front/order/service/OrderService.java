package com.nhnacademy.mini_dooray.ssacthree_front.order.service;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponseWithCount;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderSaveRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    void saveOrder(OrderSaveRequest orderSaveRequest);


    // 회원 주문 조회
    OrderResponseWithCount getOrdersByMemberAndDate(Long customerId, int page, int size, LocalDate startDate, LocalDate endDate);
}
