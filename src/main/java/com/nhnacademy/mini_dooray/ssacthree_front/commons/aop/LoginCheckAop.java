package com.nhnacademy.mini_dooray.ssacthree_front.commons.aop;


import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.NotLoginException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class LoginCheckAop {

    private final HttpServletRequest request;

    @Before("@annotation(com.nhnacademy.mini_dooray.ssacthree_front.commons.aop.annotation.LoginRequired)")
    public void checkLoginStatus() {


        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            throw new NotLoginException("로그인이 필요합니다.");
        }

    }
}
