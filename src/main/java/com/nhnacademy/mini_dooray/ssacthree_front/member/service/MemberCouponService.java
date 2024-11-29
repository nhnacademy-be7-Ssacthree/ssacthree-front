package com.nhnacademy.mini_dooray.ssacthree_front.member.service;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberCouponGetResponse;
import org.springframework.data.domain.Page;

public interface MemberCouponService {
    Page<MemberCouponGetResponse> getMemberCoupons(int page, int size, String sort, String direction);
}
