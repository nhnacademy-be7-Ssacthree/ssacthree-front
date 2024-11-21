package com.nhnacademy.mini_dooray.ssacthree_front.commons.filter;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.adapter.AuthAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.CookieUtil;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthAdapter authAdapter;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String role = null;
        if (CookieUtil.checkAccessTokenCookie(request)) {
            try {
                ResponseEntity<?> responseEntity = authAdapter.roleCheck();
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    role = "ADMIN";
                }

            } catch (FeignException e) {
                role = "MEMBER";
            }

            List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                null, null, authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);


        }

        filterChain.doFilter(request, response);
    }
}
