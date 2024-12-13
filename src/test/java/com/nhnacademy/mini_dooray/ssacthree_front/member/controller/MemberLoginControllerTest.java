package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ExtendWith(MockitoExtension.class)
class MemberLoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MemberService memberService;

    @Mock
    private CartService cartService;

    @InjectMocks
    private MemberLoginController memberLoginController;

    @BeforeEach
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(memberLoginController)
            .setViewResolvers(viewResolver)
            .build();
    }

    @Test
    void loginTest() throws Exception {

        // Given
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("test", "test");

        // When
        when(memberService.memberLogin(eq(memberLoginRequest), any(HttpServletResponse.class)))
            .thenReturn(new MessageResponse("test"));

        // Then
        mockMvc.perform(post("/login")
                .flashAttr("memberLoginRequest", memberLoginRequest))
            .andExpect(redirectedUrl("/shop/members/carts")); // 수정: "redirect:" 제거

        verify(memberService).memberLogin(eq(memberLoginRequest), any(HttpServletResponse.class));
    }

    @Test
    void logoutTest() throws Exception {

        mockMvc.perform(post("/logout"))
            .andExpect(redirectedUrl("/"));
    }

}
