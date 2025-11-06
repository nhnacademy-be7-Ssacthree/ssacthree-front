package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.adapter.CouponAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.exception.CouponGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.service.CouponService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.ResponseEntityHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponAdapter couponAdapter;

    @Override
    public List<CouponGetResponse> getAllCoupons() {
        return ResponseEntityHandler.getResponseBody(
            couponAdapter.getAllCoupons(),
            () -> new CouponGetFailedException("쿠폰 조회에 실패하였습니다.")
        );
    }

    @Override
    public MessageResponse createCoupon(CouponCreateRequest couponCreateRequest) {
        return ResponseEntityHandler.getResponseBody(
            couponAdapter.createCoupon(couponCreateRequest),
            () -> new CouponGetFailedException("쿠폰 생성에 실패하였습니다.")
        );
    }

    @Override
    public MessageResponse updateCoupon(CouponUpdateRequest couponUpdateRequest) {
        return ResponseEntityHandler.getResponseBody(
            couponAdapter.updateCoupon(couponUpdateRequest),
            () -> new CouponGetFailedException("쿠폰 수정에 실패하였습니다.")
        );
    }
}
