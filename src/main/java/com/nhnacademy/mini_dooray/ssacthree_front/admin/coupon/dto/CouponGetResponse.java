package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.domain.CouponEffectivePeriodUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class CouponGetResponse {

    private Long couponId;
    private String couponName;
    private String couponDescription;
    private int couponEffectivePeriod;
    private CouponEffectivePeriodUnit couponEffectivePeriodUnit;
    private LocalDateTime couponCreateAt;
    private LocalDateTime couponExpiredAt;
    private Long couponRuleId;
}
