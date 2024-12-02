package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberCouponGetResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class MemberCouponServiceImplTest {

    @InjectMocks
    private MemberCouponServiceImpl memberCouponService;

    @Mock
    private MemberAdapter memberAdapter;

    @Test
    void testGetMemberCouponsSuccess() {
        MockitoAnnotations.openMocks(this);

        // Mock response
        MemberCouponGetResponse mockResponse = new MemberCouponGetResponse("test", "test",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now());
        Page<MemberCouponGetResponse> mockPage = new PageImpl<>(
            Collections.singletonList(mockResponse));
        ResponseEntity<Page<MemberCouponGetResponse>> mockEntity = new ResponseEntity<>(mockPage,
            HttpStatus.OK);

        when(memberAdapter.getMemberCoupons(0, 5, "couponIssueDate", "ASC")).thenReturn(mockEntity);

        Page<MemberCouponGetResponse> result = memberCouponService.getMemberCoupons(0, 5,
            "couponIssueDate", "ASC");

        assertEquals(1, result.getTotalElements());
    }

}
