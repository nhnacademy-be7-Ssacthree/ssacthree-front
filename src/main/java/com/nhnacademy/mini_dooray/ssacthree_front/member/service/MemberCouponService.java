package com.nhnacademy.mini_dooray.ssacthree_front.member.service;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberCouponGetResponse;
import org.springframework.data.domain.Page;

public interface MemberCouponService {

    Page<MemberCouponGetResponse> getNotUsedMemberCoupons(int page, int size, String[] sort);

    Page<MemberCouponGetResponse> getUsedMemberCoupons(int page, int size, String[] sort);
}
