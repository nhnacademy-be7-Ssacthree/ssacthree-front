package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.adapter.CouponRuleAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.exception.CouponRuleCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.exception.CouponRuleGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.service.CouponRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.ResponseEntityHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponRuleServiceImpl implements CouponRuleService {

    private final CouponRuleAdapter couponRuleAdapter;
    private static final String COUPON_RULE_FIND_FAILED = "쿠폰 정책 조회에 실패하였습니다.";

    @Override
    public List<CouponRuleGetResponse> getAllCouponRules() {
        return ResponseEntityHandler.getResponseBody(
            couponRuleAdapter.getAllCouponRules(),
            () -> new CouponRuleGetFailedException(COUPON_RULE_FIND_FAILED)
        );
    }

    @Override
    public List<CouponRuleGetResponse> getAllSelectedCouponRules() {
        return ResponseEntityHandler.getResponseBody(
            couponRuleAdapter.getAllSelectedCouponRules(),
            () -> new CouponRuleGetFailedException(COUPON_RULE_FIND_FAILED)
        );
    }

    @Override
    public MessageResponse createCouponRule(CouponRuleCreateRequest couponRuleCreateRequest) {
        return ResponseEntityHandler.getResponseBody(
            couponRuleAdapter.createCouponRule(couponRuleCreateRequest),
            () -> new CouponRuleCreateFailedException("쿠폰 정책 생성에 실패하였습니다.")
        );
    }

    @Override
    public MessageResponse updateCouponRule(CouponRuleUpdateRequest couponRuleUpdateRequest) {
        return ResponseEntityHandler.getResponseBody(
            couponRuleAdapter.updateCouponRule(couponRuleUpdateRequest),
            () -> new CouponRuleCreateFailedException("쿠폰 정책 수정에 실패하였습니다.")
        );
    }
}
