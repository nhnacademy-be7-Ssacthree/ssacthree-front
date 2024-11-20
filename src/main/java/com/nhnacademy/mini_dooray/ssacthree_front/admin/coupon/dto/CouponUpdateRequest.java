package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class CouponUpdateRequest {

    @NotNull
    private Long couponId;
}
