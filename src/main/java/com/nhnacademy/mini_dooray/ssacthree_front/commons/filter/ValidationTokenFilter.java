package com.nhnacademy.mini_dooray.ssacthree_front.commons.filter;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.adapter.AuthAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.InvalidTokenException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.CookieUtil;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 토큰이 유효한지 검사하는 필터 (블랙리스트 토큰 체크..)
 */
@Slf4j
@RequiredArgsConstructor
public class ValidationTokenFilter extends OncePerRequestFilter {

    private final AuthAdapter authAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
        HttpServletResponse httpSevletResponse,
        FilterChain filterChain) throws ServletException, IOException {

        // 요청 URI 확인
        String uri = httpServletRequest.getRequestURI();

        // 정적 자원 경로 제외 (예: 이미지, CSS, JS)
        if (uri.startsWith("/images") || uri.startsWith("/css") || uri.startsWith("/js")) {
            filterChain.doFilter(httpServletRequest, httpSevletResponse);
            return;
        }

        if (!CookieUtil.checkAccessTokenCookie(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, httpSevletResponse);
            return;
        }

        try {
            ResponseEntity<?> response = authAdapter.validateToken();

            if (response.getStatusCode().is2xxSuccessful()) {
                filterChain.doFilter(httpServletRequest, httpSevletResponse);
                return;
            }
        } catch (FeignException e) {
            httpSevletResponse.addCookie(CookieUtil.cookieDestroyer("access-token"));
            httpSevletResponse.addCookie(CookieUtil.cookieDestroyer("refresh-token"));
            throw new InvalidTokenException("유효하지 않은 토큰");
        }


    }
}
