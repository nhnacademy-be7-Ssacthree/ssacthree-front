package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberCouponGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

    private final MemberAdapter memberAdapter;

    @Override
    public Page<MemberCouponGetResponse> getNotUsedMemberCoupons(int page, int size, String[] sort) {
        try {
            ResponseEntity<Page<MemberCouponGetResponse>> responseEntity = memberAdapter.getNotUsedMemberCoupons(page, size, sort);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                throw new RuntimeException("API 호출 실패: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("API 호출 중 예외 발생", e);
        }
    }

    @Override
    public Page<MemberCouponGetResponse> getUsedMemberCoupons(int page, int size, String[] sort) {
        try {
            ResponseEntity<Page<MemberCouponGetResponse>> responseEntity = memberAdapter.getUsedMemberCoupons(page, size, sort);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                throw new RuntimeException("API 호출 실패: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("API 호출 중 예외 발생", e);
        }
    }
}
