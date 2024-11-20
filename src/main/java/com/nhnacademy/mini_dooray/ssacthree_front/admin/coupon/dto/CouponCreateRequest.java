package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.domain.CouponEffectivePeriodUnit;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class CouponCreateRequest {

    @NotBlank
    @Size(max = 30)
    private String couponName;

    @NotBlank
    private String couponDescription;

    private int couponEffectivePeriod;

    private CouponEffectivePeriodUnit couponEffectivePeriodUnit;

    private LocalDateTime couponExpiredAt;

    @NotNull
    private Long couponRuleId;
}
