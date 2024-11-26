package com.nhnacademy.mini_dooray.ssacthree_front.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "member-like")
public class UrlConfig {
    private String url;
}
