package com.nhnacademy.mini_dooray.ssacthree_front.order.adapter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name="gateway-service", url="${member.url}")
public interface OrderAdapter {

    /**
     * 비회원 장바구니 주문 - 장바구니 정보 가져오기 redis 이용
     * @param
     * @return 비회원 장바구니 정보는 근데 사실 redis라서 ..?
     */
//    @GetMapping
//    ResponseEntity<GuestCartResponse> getGeustCartInfo() {
//
//    }


    // 비회원 바로 주문 - 책 정보 가져오기 - 책만 가져오면 ok (hidden)

    // 회원 장바구니 주문 - 장바구니 정보 가져오기

    // 회원 바로 주문 - 책 정보 가져오기

    // 비회원 주문 내역 - 상세 조회

    // 회원 주문 내역 - 전체 조회

    // 회원 주문 내역 - 상세 조회


}
