package com.nhnacademy.mini_dooray.ssacthree_front.commons.security;

import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.NotLoginException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 예외 정보를 리퀘스트에 저장
        request.setAttribute("exception", new NotLoginException("로그인 후 이용 가능합니다."));

        // Spring MVC로 요청을 전달
        request.getRequestDispatcher("/error/unauthorized").forward(request, response);
    }
}
