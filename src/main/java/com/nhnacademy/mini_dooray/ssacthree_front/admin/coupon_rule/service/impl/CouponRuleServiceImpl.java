package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.adapter.CouponRuleAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.exception.CouponRuleCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.exception.CouponRuleGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.service.CouponRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponRuleServiceImpl implements CouponRuleService {

    private final CouponRuleAdapter couponRuleAdapter;

    @Override
    public List<CouponRuleGetResponse> getAllCouponRules() {
        ResponseEntity<List<CouponRuleGetResponse>> response = couponRuleAdapter.getAllCouponRules();

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponRuleGetFailedException("쿠폰 정책 조회에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponRuleGetFailedException("쿠폰 정책 조회에 실패하였습니다.");
        }
    }

    @Override
    public List<CouponRuleGetResponse> getAllSelectedCouponRules() {
        ResponseEntity<List<CouponRuleGetResponse>> response = couponRuleAdapter.getAllSelectedCouponRules();

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponRuleGetFailedException("쿠폰 정책 조회에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponRuleGetFailedException("쿠폰 정책 조회에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse createCouponRule(CouponRuleCreateRequest couponRuleCreateRequest) {
        ResponseEntity<MessageResponse> response = couponRuleAdapter.createCouponRule(couponRuleCreateRequest);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponRuleCreateFailedException("쿠폰 정책 생성에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponRuleCreateFailedException("쿠폰 정책 생성에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse updateCouponRule(CouponRuleUpdateRequest couponRuleUpdateRequest) {
        ResponseEntity<MessageResponse> response = couponRuleAdapter.updateCouponRule(couponRuleUpdateRequest);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponRuleCreateFailedException("쿠폰 정책 수정에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new CouponRuleCreateFailedException("쿠폰 정책 수정에 실패하였습니다.");
        }
    }
}
