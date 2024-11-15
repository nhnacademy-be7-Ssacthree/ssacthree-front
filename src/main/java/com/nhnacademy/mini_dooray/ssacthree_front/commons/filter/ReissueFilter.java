package com.nhnacademy.mini_dooray.ssacthree_front.commons.filter;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.adapter.AuthAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j
@RequiredArgsConstructor
public class ReissueFilter extends OncePerRequestFilter {

    private final AuthAdapter adapter;

    private static final String SET_COOKIE_HEADER = "Set-Cookie";


    @Override
    public void doFilterInternal(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
        FilterChain filterChain) throws IOException, ServletException {

        // 요청 URI 확인
        String uri = httpRequest.getRequestURI();

        // 정적 자원 경로 제외 (예: 이미지, CSS, JS)
        if (uri.startsWith("/images") || uri.startsWith("/css") || uri.startsWith("/js")) {
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        }

        String accessToken = null;
        String refreshToken = null;

        if (httpRequest.getCookies() != null) {
            for (Cookie cookie : httpRequest.getCookies()) {
                if (cookie.getName().equals("access-token")) {
                    accessToken = cookie.getValue();
                }
                if (cookie.getName().equals("refresh-token")) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        try {
            if (accessToken == null && refreshToken != null) {
                ResponseEntity<MessageResponse> response = adapter.reissueToken();
                List<String> cookies = response.getHeaders().get(SET_COOKIE_HEADER);
                assert cookies != null;
                httpResponse.addHeader(SET_COOKIE_HEADER, cookies.get(0));
                httpResponse.addHeader(SET_COOKIE_HEADER, cookies.get(1));
            }
            filterChain.doFilter(httpRequest, httpResponse);
            return;

        } catch (Exception e) {
            httpResponse.sendRedirect("/");
        }


    }


}
