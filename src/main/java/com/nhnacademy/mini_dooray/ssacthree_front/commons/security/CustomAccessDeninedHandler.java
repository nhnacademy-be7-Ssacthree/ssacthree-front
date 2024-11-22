package com.nhnacademy.mini_dooray.ssacthree_front.commons.security;

import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.IllegalAccessException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class CustomAccessDeninedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 예외 정보를 리퀘스트에 저장
        request.setAttribute("exception", new IllegalAccessException("권한이 없습니다."));

        // Spring MVC로 요청을 전달
        request.getRequestDispatcher("/error/forbidden").forward(request, response);
    }
}
