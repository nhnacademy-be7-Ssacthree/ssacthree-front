package com.nhnacademy.mini_dooray.ssacthree_front.commons.filter;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.adapter.AuthAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.InvalidTokenException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.filter.OncePerRequestFilter;


@RequiredArgsConstructor
@Order(0)
public class ValidationTokenFilter extends OncePerRequestFilter {

    private final AuthAdapter authAdapter;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
        HttpServletResponse httpSevletResponse,
        FilterChain filterChain) throws ServletException, IOException {

        if (CookieUtil.checkAccessTokenCookie(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, httpSevletResponse);
        }

        ResponseEntity<?> response = authAdapter.validateToken();

        if (response.getStatusCode().is2xxSuccessful()) {
            filterChain.doFilter(httpServletRequest, httpSevletResponse);
        }

        throw new InvalidTokenException("유효하지 않은 토큰");

    }
}
