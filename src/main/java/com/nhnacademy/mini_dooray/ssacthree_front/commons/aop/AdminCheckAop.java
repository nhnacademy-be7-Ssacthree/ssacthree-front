package com.nhnacademy.mini_dooray.ssacthree_front.commons.aop;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.adapter.AuthAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AdminCheckAop {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final AuthAdapter authAdapter;

    @Before("@annotation(com.nhnacademy.mini_dooray.ssacthree_front.commons.aop.annotation.Admin)")
    public void checkAdmin() {

        if (CookieUtil.checkAccessTokenCookie(request)) {
            throw new RuntimeException("로그인 해야합니다");
        }

        authAdapter.roleCheck();
    }

}
