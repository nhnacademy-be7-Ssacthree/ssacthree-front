package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;

import java.util.List;

public interface CouponService {
    List<CouponGetResponse> getAllCoupons();

    MessageResponse createCoupon(CouponCreateRequest couponCreateRequest);

    MessageResponse updateCoupon(CouponUpdateRequest couponUpdateRequest);
}
