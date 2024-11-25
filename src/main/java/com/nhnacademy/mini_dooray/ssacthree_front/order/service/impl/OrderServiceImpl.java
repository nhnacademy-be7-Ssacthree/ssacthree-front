package com.nhnacademy.mini_dooray.ssacthree_front.order.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.exception.PackagingCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.adapter.OrderAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderAdapter orderAdapter;

    @Override
    public OrderResponse createOrder(OrderSaveRequest orderSaveRequest) {
        ResponseEntity<OrderResponse> response = orderAdapter.createOrder(orderSaveRequest);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }

        // 적적한 예외로 교체
        throw new RuntimeException("주문 저장 에러");

    }
}
