package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.adapter.CouponAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.exception.CouponGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.service.CouponService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponAdapter couponAdapter;

    @Override
    public List<CouponGetResponse> getAllCoupons() {
        ResponseEntity<List<CouponGetResponse>> response = couponAdapter.getAllCoupons();

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponGetFailedException("쿠폰 조회에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponGetFailedException("쿠폰 조회에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse createCoupon(CouponCreateRequest couponCreateRequest) {
        ResponseEntity<MessageResponse> response = couponAdapter.createCoupon(couponCreateRequest);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponGetFailedException("쿠폰 생성에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponGetFailedException("쿠폰 생성에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse updateCoupon(CouponUpdateRequest couponUpdateRequest) {
        ResponseEntity<MessageResponse> response = couponAdapter.updateCoupon(couponUpdateRequest);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponGetFailedException("쿠폰 수정에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponGetFailedException("쿠폰 수정에 실패하였습니다.");
        }
    }
}
