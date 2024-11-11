package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.member.controller.MemberMyPageController;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.mockito.ArgumentMatchers.any;

class MemberMyPageControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberMyPageController memberMyPageController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberMyPageController).build();
    }

    @Test
    void testMyPage() throws Exception {
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
        when(memberService.getMemberInfo(any(HttpServletRequest.class))).thenReturn(memberInfoResponse);

        mockMvc.perform(get("/members/my-page"))
            .andExpect(view().name("myPage"))
            .andExpect(model().attribute("memberInfo", memberInfoResponse));

        verify(memberService, times(2)).getMemberInfo(any(HttpServletRequest.class));
    }

    @Test
    void testUpdateUser() throws Exception {
        MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest();

        mockMvc.perform(post("/members/my-page/update")
                .flashAttr("memberInfoUpdateRequest", updateRequest))
            .andExpect(redirectedUrl("/members/my-page"));

        verify(memberService, times(1)).memberInfoUpdate(eq(updateRequest), any(HttpServletRequest.class));
    }

//    @Test
//    void testWithdraw() throws Exception {
//        mockMvc.perform(post("/members/withdraw"))
//            .andExpect(redirectedUrl("/"));
//
//        verify(memberService, times(1)).memberWithdraw(any(HttpServletRequest.class));
//    }
}