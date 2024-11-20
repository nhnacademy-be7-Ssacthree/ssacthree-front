package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class PaycoLoginController {


    @Value("${payco.url}")
    private String url;

    @Value("${payco.client_id}")
    private String clientId;

    @Value("${payco.response_type}")
    private String responseType;

    @Value("${payco.redirect_url}")
    private String redirectUrl;

    @Value("${payco.service_provider_code}")
    private String serviceProviderCode;

    @Value("${payco.user_locale}")
    private String userLocale;

    @GetMapping("/payco-login")
    public String paycoLogin() {
        StringBuilder params = new StringBuilder("?");
        params.append("client_id=").append(clientId)
            .append("&response_type=").append(responseType)
            .append("&redirect_uri=").append(redirectUrl)
            .append("&serviceProviderCode=").append(serviceProviderCode)
            .append("&userLocale=").append(userLocale);
        return "redirect:" + url + params.toString();
    }

    @GetMapping("/payco/callback")
    public String paycoLoginCallback(@RequestParam(name = "code") String code) {
        // TODO : access-token 발급 받는거 부터 해야뎀.. ㅠ
        return null;
    }

}
