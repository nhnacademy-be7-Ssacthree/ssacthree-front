package com.nhnacademy.mini_dooray.ssacthree_front.commons.filter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.adapter.AuthAdapter;
import feign.FeignException;
import feign.Request;
import feign.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import java.io.IOException;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

class AuthenticationFilterTest {

    private AuthAdapter authAdapter;
    private AuthenticationFilter authenticationFilter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        authAdapter = mock(AuthAdapter.class);
        authenticationFilter = new AuthenticationFilter(authAdapter);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        filterChain = mock(FilterChain.class);
        SecurityContextHolder.clearContext(); // Ensure clean state
    }

    @Test
    void doFilterInternal_withValidAdminToken() throws ServletException, IOException {
        // Given
        Cookie accessTokenCookie = new Cookie("access-token", "valid-token");
        request.setCookies(accessTokenCookie);
        when(authAdapter.roleCheck()).thenReturn(ResponseEntity.ok().build());

        // When
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(authAdapter, times(1)).roleCheck();
        assertThat(SecurityContextHolder.getContext().getAuthentication())
            .isInstanceOf(UsernamePasswordAuthenticationToken.class)
            .satisfies(auth -> assertThat(
                auth.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo(
                "ROLE_ADMIN"));
    }

    @Test
    void doFilterInternal_withInvalidToken() throws ServletException, IOException {
        // Given
        Cookie accessTokenCookie = new Cookie("access-token", "invalid-token");
        request.setCookies(accessTokenCookie);

        // Simulate FeignException when roleCheck is called
        when(authAdapter.roleCheck()).thenThrow(
            FeignException.errorStatus("roleCheck",
                Response.builder()
                    .status(403)
                    .reason("Forbidden")
                    .request(
                        Request.create(Request.HttpMethod.POST, "/auth/admin", Map.of(), null, null,
                            null))
                    .build()
            )
        );

        // When
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        // Verify that roleCheck was called
        verify(authAdapter, times(1)).roleCheck();

        // Verify SecurityContext has "ROLE_MEMBER" due to exception
        assertThat(SecurityContextHolder.getContext().getAuthentication())
            .isInstanceOf(UsernamePasswordAuthenticationToken.class)
            .satisfies(auth -> assertThat(
                auth.getAuthorities().stream().findFirst().get().getAuthority()).isEqualTo(
                "ROLE_MEMBER"));

        // Ensure filter chain continues execution
        verify(filterChain, times(1)).doFilter(request, response);
    }


    @Test
    void doFilterInternal_staticResources() throws ServletException, IOException {
        // Given
        request.setRequestURI("/images/logo.png");

        // When
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(authAdapter, never()).roleCheck();
        verify(filterChain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    void doFilterInternal_noAccessTokenCookie() throws ServletException, IOException {
        // Given
        request.setCookies(); // No cookies set

        // When
        authenticationFilter.doFilterInternal(request, response, filterChain);

        // Then
        verify(authAdapter, never()).roleCheck();
        verify(filterChain, times(1)).doFilter(request, response);
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }
}
