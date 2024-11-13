package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CouponRuleUpdateRequest {

    @NotNull
    private Long couponRuleId;
}