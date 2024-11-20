package com.nhnacademy.mini_dooray.ssacthree_front.commons.aop;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.adapter.AuthAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.CookieUtil;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AdminCheckAop {

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final AuthAdapter authAdapter;

    @Before("@annotation(com.nhnacademy.mini_dooray.ssacthree_front.commons.aop.annotation.Admin)")
    public void checkAdmin() {

        if (!CookieUtil.checkAccessTokenCookie(request)) {
            throw new RuntimeException("잘못된 접근입니다.");
        }
        try {
            if (authAdapter.roleCheck().getStatusCode().is2xxSuccessful()) {
                return;
            }
        } catch (FeignException e) {
            log.info(e.getMessage());
            throw e;
        }


    }

}
