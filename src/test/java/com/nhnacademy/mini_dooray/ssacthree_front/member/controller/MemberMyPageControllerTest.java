package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MemberMyPageControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberMyPageController memberMyPageController;

    private MockMvc mockMvc;

    private MemberInfoUpdateRequest validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberMyPageController).build();
        validRequest = new MemberInfoUpdateRequest();
        validRequest.setCustomerPhoneNumber("01036220514");
    }

    @Test
    void testMyPage() throws Exception {
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
        when(memberService.getMemberInfo(any(HttpServletRequest.class))).thenReturn(
            memberInfoResponse);

        mockMvc.perform(get("/members/my-page"))
            .andExpect(view().name("myPage"))
            .andExpect(model().attribute("memberInfo", memberInfoResponse));

        verify(memberService, times(1)).getMemberInfo(any(HttpServletRequest.class));
    }


    @Test
    void testUpdateUser_Success() throws Exception {
        // 유효한 값 설정
        mockMvc.perform(post("/members/my-page/update")
                .flashAttr("memberInfoUpdateRequest", validRequest))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/members/my-page"));

    }

    @Test
    void testUpdateUser_ValidationError() throws Exception {
        // 유효하지 않은 요청 객체 생성
        MemberInfoUpdateRequest invalidRequest = new MemberInfoUpdateRequest();
        invalidRequest.setCustomerPhoneNumber("123"); // 잘못된 전화번호 형식

        mockMvc.perform(post("/members/my-page/update")
                .flashAttr("memberInfoUpdateRequest", invalidRequest))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/members/my-page"))
            .andExpect(flash().attributeExists("error"));

        // 서비스 호출이 이루어지지 않았음을 확인
        verify(memberService, times(0)).memberInfoUpdate(any(), any());
    }


    @Test
    void testWithdraw() throws Exception {
        mockMvc.perform(post("/members/withdraw"))
            .andExpect(redirectedUrl("/"));

        verify(memberService, times(1)).memberWithdraw(any(HttpServletRequest.class), any(
            HttpServletResponse.class));
    }

}