package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.domain.CouponType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CouponRuleGetResponse {

    private Long couponRuleId;
    private CouponType couponType;
    private int couponMinOrderPrice;
    private int maxDiscountPrice;
    private int couponDiscountPrice;
    private String couponRuleName;
    private boolean couponIsUsed;
    private LocalDateTime couponRuleCreatedAt;
}