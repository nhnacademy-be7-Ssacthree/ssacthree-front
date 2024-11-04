package com.nhnacademy.mini_dooray.ssacthree_front.config;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.interceptor.FeignCookieInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FeignClient에 쿠키를 담기위한 인터셉터 등록 config
 * @author : 김희망
 * @date : 2024/11/03
 *
 */
@Configuration
public class FeignConfig {
    @Bean
    public RequestInterceptor requestInterceptor() {
        return new FeignCookieInterceptor();
    }
}

