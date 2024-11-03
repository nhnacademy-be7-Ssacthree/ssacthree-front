package com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class FeignCookieInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Cookie[] cookies = httpServletRequest.getCookies();

        if(cookies != null) {
            StringBuilder cookieBuilder = new StringBuilder();
            for (Cookie cookie : cookies) {
                cookieBuilder.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
            }
            requestTemplate.header("Cookie", cookieBuilder.toString());
        }
    }
}
