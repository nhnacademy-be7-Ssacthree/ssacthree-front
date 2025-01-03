package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberCouponGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.CouponFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberCouponService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberCouponServiceImpl implements MemberCouponService {

    private final MemberAdapter memberAdapter;

    @Override
    public Page<MemberCouponGetResponse> getMemberCoupons(int page, int size, String sort, String direction) {

        try {
            ResponseEntity<Page<MemberCouponGetResponse>> response = memberAdapter.getMemberCoupons(
                    page, size, sort, direction);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new CouponFailedException("쿠폰을 가져오는데 실패하였습니다.");
        } catch (FeignException e) {
            throw new CouponFailedException("API 호출 중 예외 발생");
        }
    }
}
