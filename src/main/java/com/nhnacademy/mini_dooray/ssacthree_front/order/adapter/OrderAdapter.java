package com.nhnacademy.mini_dooray.ssacthree_front.order.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderSaveRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="gateway-service", url="${member.url}")
public interface OrderAdapter {

    // 주문 저장
    @PostMapping("/shop/orders")
    ResponseEntity<OrderResponse> createOrder(@RequestBody OrderSaveRequest orderSaveRequest);

    // 비회원 주문 내역 - 상세 조회

    // 회원 주문 내역 - 전체 조회

    // 회원 주문 내역 - 상세 조회


}
