package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.mini_dooray.ssacthree_front.member.service.PaycoService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

// 테스트 클래스 선언
class PaycoLoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PaycoService paycoService;

    @InjectMocks
    private PaycoLoginController paycoLoginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paycoLoginController).build();
    }

    @Test
    void testPaycoLogin() throws Exception {
        String redirectUrl = "redirect:https://auth.payco.com/oauth2.0/authorize?client_id=abc";
        when(paycoService.getAuthorizationCodeUrl()).thenReturn(redirectUrl);

        mockMvc.perform(get("/payco-login"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("https://auth.payco.com/oauth2.0/authorize?client_id=abc"));

        verify(paycoService, times(1)).getAuthorizationCodeUrl();
    }

    @Test
    void testPaycoConnection() throws Exception {
        String redirectUrl = "redirect:https://auth.payco.com/oauth2.0/authorize?client_id=abc&connection=true";
        when(paycoService.getAuthorizationCodeForConnection()).thenReturn(redirectUrl);

        mockMvc.perform(get("/members/payco-connection"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl(
                "https://auth.payco.com/oauth2.0/authorize?client_id=abc&connection=true"));

        verify(paycoService, times(1)).getAuthorizationCodeForConnection();
    }

    @Test
    void testPaycoLoginCallback() throws Exception {
        String code = "test_code";
        String accessToken = "test_access_token";
        String paycoIdNo = "test_payco_id_no";
        String loginResult = "Login Successful"; // Or any expected return value

        when(paycoService.getAccessToken(code)).thenReturn(accessToken);
        when(paycoService.getPaycoIdNo(accessToken)).thenReturn(paycoIdNo);
        when(paycoService.paycoLogin(eq(paycoIdNo), any(HttpServletResponse.class))).thenReturn(
            loginResult);

        mockMvc.perform(get("/payco/callback").param("code", code))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/shop/members/carts"));

        verify(paycoService, times(1)).getAccessToken(code);
        verify(paycoService, times(1)).getPaycoIdNo(accessToken);
        verify(paycoService, times(1)).paycoLogin(eq(paycoIdNo), any(HttpServletResponse.class));
    }

    @Test
    void testPaycoConnectionCallback() throws Exception {
        String code = "test_code";
        String accessToken = "test_access_token";
        String paycoIdNo = "test_payco_id_no";
        String connectionResult = "연동 성공";

        when(paycoService.getAccessToken(code)).thenReturn(accessToken);
        when(paycoService.getPaycoIdNo(accessToken)).thenReturn(paycoIdNo);
        when(paycoService.paycoConnect(paycoIdNo)).thenReturn(connectionResult);

        mockMvc.perform(get("/members/payco-connection/callback").param("code", code))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/members/my-page"))
            .andExpect(flash().attribute("connectionResult", connectionResult));

        verify(paycoService, times(1)).getAccessToken(code);
        verify(paycoService, times(1)).getPaycoIdNo(accessToken);
        verify(paycoService, times(1)).paycoConnect(paycoIdNo);
    }
}
