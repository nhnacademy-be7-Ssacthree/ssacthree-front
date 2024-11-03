package com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

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
