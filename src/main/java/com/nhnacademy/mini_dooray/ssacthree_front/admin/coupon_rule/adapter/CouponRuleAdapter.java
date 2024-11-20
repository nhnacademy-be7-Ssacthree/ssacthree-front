package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="gateway-service", url = "${admin-client.url}", contextId = "couponRuleClient")
public interface CouponRuleAdapter {
    @GetMapping("/coupon-rules")
    ResponseEntity<List<CouponRuleGetResponse>> getAllCouponRules();

    @GetMapping("/coupon-rules/selected")
    ResponseEntity<List<CouponRuleGetResponse>> getAllSelectedCouponRules();

    @PutMapping("/coupon-rules")
    ResponseEntity<MessageResponse> updateCouponRule(@RequestBody CouponRuleUpdateRequest couponRuleUpdateRequest);

    @PostMapping("/coupon-rules")
    ResponseEntity<MessageResponse> createCouponRule(@RequestBody CouponRuleCreateRequest couponRuleCreateRequest);
}
