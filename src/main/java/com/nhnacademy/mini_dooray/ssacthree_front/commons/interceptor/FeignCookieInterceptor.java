package com.nhnacademy.mini_dooray.ssacthree_front.commons.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Objects;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


/**
 * FiegnClient에 쿠키를 담기 위한 인터셉터 요청 쏠 때
 * @author : 김희망
 * @Date : 2024/11/03
 */
public class FeignCookieInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        // RequestAttributes가 없는 경우 처리
        if (requestAttributes == null) {
            // Redis 리스너나 비동기 환경에서는 HTTP 컨텍스트가 없을 수 있습니다.
            return;
        }


        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) Objects.requireNonNull(
            RequestContextHolder.getRequestAttributes())).getRequest();

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
