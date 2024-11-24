package com.nhnacademy.mini_dooray.ssacthree_front.member.service;

import jakarta.servlet.http.HttpServletResponse;

public interface PaycoService {

    String getAuthorizationCodeUrl();

    String getAuthorizationCodeForConnection();

    String getAccessToken(String code);

    String getPaycoIdNo(String accessToken);

    String paycoLogin(String paycoIdNo, HttpServletResponse httpServletResponse);

    String paycoConnect(String paycoIdNo);
}
