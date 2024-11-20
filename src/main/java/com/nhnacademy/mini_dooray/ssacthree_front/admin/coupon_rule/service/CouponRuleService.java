package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;

import java.util.List;

public interface CouponRuleService {
    List<CouponRuleGetResponse> getAllCouponRules();

    List<CouponRuleGetResponse> getAllSelectedCouponRules();

    MessageResponse createCouponRule(CouponRuleCreateRequest couponRuleCreateRequest);

    MessageResponse updateCouponRule(CouponRuleUpdateRequest couponRuleUpdateRequest);
}
