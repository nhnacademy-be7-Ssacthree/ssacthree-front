package com.nhnacademy.mini_dooray.ssacthree_front.commons.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * 로그인을 체크하기위한 interceptor - (FE header)
 * @author : 김희망
 * @Date : 2024/11/03
 */
public class CheckLoginInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
        ModelAndView modelAndView) throws Exception {

        if(modelAndView != null) {
            boolean accessTokenExists = false;
            if(request.getCookies() != null) {
                for(Cookie cookie : request.getCookies()) {
                    if(cookie.getName().equals("access-token")) {
                        accessTokenExists = true;
                    }
                }
            }
            modelAndView.addObject("accessTokenExists", accessTokenExists);
        }
    }
}
