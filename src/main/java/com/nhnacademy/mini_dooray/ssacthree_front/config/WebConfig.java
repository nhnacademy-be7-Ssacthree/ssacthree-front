package com.nhnacademy.mini_dooray.ssacthree_front.config;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.interceptor.CheckLoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * web config
 * @author : 김희망
 * @Date : 2024/11/03
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CheckLoginInterceptor())
            .addPathPatterns("/**");
    }
}
