package com.nhnacademy.mini_dooray.ssacthree_front.order.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.order.adapter.OrderAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponseWithCount;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderAdapter orderAdapter;

    @Override
    public void saveOrder(OrderSaveRequest orderSaveRequest) {
        orderAdapter.saveOrder(orderSaveRequest);
        return;

    }

    @Override
    public OrderResponseWithCount getOrdersByMemberAndDate(Long customerId, int page, int size, LocalDate startDate, LocalDate endDate) {
        return orderAdapter.getMemberOrders(customerId, page, size, startDate, endDate);
    }
}
