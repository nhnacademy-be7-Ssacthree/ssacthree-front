package com.nhnacademy.mini_dooray.ssacthree_front.member.service;

public interface PaycoService {

    String getAuthorizationCodeUrl();

    String getAccessToken(String code);

    String getPaycoIdNo(String accessToken);
}
