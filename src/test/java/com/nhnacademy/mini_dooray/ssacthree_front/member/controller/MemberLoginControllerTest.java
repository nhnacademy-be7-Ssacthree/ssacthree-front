package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MemberLoginControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberLoginController memberLoginController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberLoginController).build();
    }

//    @Test
//    void testLogin() throws Exception {
//        MemberLoginRequest loginRequest = new MemberLoginRequest("test", "test");
//
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        when(memberService.memberLogin(any(MemberLoginRequest.class),
//            any(HttpServletResponse.class))).thenReturn(null);
//
//        mockMvc.perform(post("/login")
//                .flashAttr("requestBody", loginRequest))
//            .andExpect(redirectedUrl("/"));
//
//        verify(memberService, times(1)).memberLogin(any(MemberLoginRequest.class),
//            any(HttpServletResponse.class));
//    }

    @Test
    void testLogout() throws Exception {
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Perform the logout request
        mockMvc.perform(post("/logout"))
            .andExpect(redirectedUrl("/"));

        verify(memberService, times(1)).memberLogout(any(HttpServletResponse.class));
    }
}
