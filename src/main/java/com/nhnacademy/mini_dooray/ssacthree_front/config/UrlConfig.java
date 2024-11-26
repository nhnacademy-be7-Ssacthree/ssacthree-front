package com.nhnacademy.mini_dooray.ssacthree_front.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UrlConfig {
    @Value("${member-like.url}")
    private String memberUrl;

    @Bean
    public String memberUrl() {
        return memberUrl;
    }
}
