package com.nhnacademy.mini_dooray.ssacthree_front.order.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderDetailResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponseWithCount;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.*;

import java.time.LocalDate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="gateway-service", url="${member.url}")
public interface OrderAdapter {

    // 주문 저장
    @PostMapping("/shop/orders")
    ResponseEntity<OrderResponse> createOrder(@RequestBody OrderSaveRequest orderSaveRequest);

    // 비회원 주문 내역 - 상세 조회

    // 회원 주문 내역 - 전체 조회
    // 회원 주문 내역 - 페이징 및 날짜 범위 조회
    @GetMapping("/shop/orders")
    OrderResponseWithCount getMemberOrders(
            @RequestParam("customerId") Long customerId,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);

    // 관리자의 주문 내역 조회
    @GetMapping("/shop/admin/orders")
    AdminOrderResponseWithCount adminGetAllOrders(
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate);


    // 회원, 비회원의 주문 상세 조회
    @GetMapping("/shop/orderDetail")
    ResponseEntity<OrderDetailResponse> getOrderDetail(@RequestParam Long orderId);

    // 주문내역의 배송 상태 변경
    @PostMapping("/shop/admin/orders/change")
    ResponseEntity<MessageResponse> changeOrderStatus(@RequestBody ChangeOrderStatusRequest request);

    // 회원, 비회원이 접근 가능한 주문 상세 조회
    @GetMapping("/shop/orderDetail/orderNumber")
    ResponseEntity<OrderDetailResponse> getOrderDetailByOrderNumber(@RequestParam String orderNumber, @RequestParam String phoneNumber);

    // 회원 주문 내역 - 상세 조회

}
