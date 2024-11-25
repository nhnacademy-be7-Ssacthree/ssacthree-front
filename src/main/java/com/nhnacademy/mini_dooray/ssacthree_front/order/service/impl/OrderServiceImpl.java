package com.nhnacademy.mini_dooray.ssacthree_front.order.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.order.adapter.OrderAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private OrderAdapter orderAdapter;

    @Override
    public void saveOrder(OrderSaveRequest orderSaveRequest) {
        orderAdapter.saveOrder(orderSaveRequest);
        return;

    }
}
