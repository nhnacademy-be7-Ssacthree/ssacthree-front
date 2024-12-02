package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberCouponGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberCouponService;
import java.time.LocalDateTime;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberCouponController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberCouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberCouponService memberCouponService;

    @Test
    void testCoupon() throws Exception {
        // Mock response
        MemberCouponGetResponse mockResponse = new MemberCouponGetResponse("test", "test",
            LocalDateTime.now(),
            LocalDateTime.now(),
            LocalDateTime.now());
        Page<MemberCouponGetResponse> mockPage = new PageImpl<>(
            Collections.singletonList(mockResponse));

        when(memberCouponService.getMemberCoupons(0, 5, "couponIssueDate", "ASC"))
            .thenReturn(mockPage);

        // Perform GET request
        mockMvc.perform(get("/members/my-page/coupons")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "couponIssueDate")
                .param("direction", "ASC"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("memberCoupons"))
            .andExpect(view().name("memberCoupon"));
    }
}
