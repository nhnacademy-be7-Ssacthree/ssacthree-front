package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class MemberCouponGetResponse {

    private String couponName;
    private String couponDescription;
    private LocalDateTime memberCouponCreatedAt;
    private LocalDateTime memberCouponExpiredAt;
    private LocalDateTime memberCouponUsedAt;
}
