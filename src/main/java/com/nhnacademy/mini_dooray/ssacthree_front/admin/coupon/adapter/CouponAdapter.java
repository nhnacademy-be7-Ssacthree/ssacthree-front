package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="gateway-service", url = "${admin-client.url}", contextId = "couponClient")
public interface CouponAdapter {
    @GetMapping("/coupons")
    ResponseEntity<List<CouponGetResponse>> getAllCoupons();

    @GetMapping("/coupons")
    ResponseEntity<MessageResponse> updateCoupon(@RequestBody CouponUpdateRequest couponUpdateRequest);

    @GetMapping("/coupons")
    ResponseEntity<MessageResponse> createCoupon(@RequestBody CouponCreateRequest couponCreateRequest);
}
