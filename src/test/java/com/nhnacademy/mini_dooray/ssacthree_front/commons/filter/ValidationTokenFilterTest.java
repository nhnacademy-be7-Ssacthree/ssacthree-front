package com.nhnacademy.mini_dooray.ssacthree_front.commons.filter;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.adapter.AuthAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.InvalidTokenException;
import feign.FeignException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ValidationTokenFilterTest {

    private AuthAdapter authAdapter;
    private ValidationTokenFilter validationTokenFilter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        authAdapter = mock(AuthAdapter.class);
        validationTokenFilter = new ValidationTokenFilter(authAdapter);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @Test
    void testFilterWithValidToken() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(request.getCookies()).thenReturn(
            new Cookie[]{new Cookie("access-token", "validToken")});
        when(authAdapter.validateToken()).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act
        validationTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testFilterWithoutAccessTokenCookie() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(request.getCookies()).thenReturn(null);

        // Act
        validationTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(authAdapter);
    }

    @Test
    void testFilterWithInvalidToken() {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/some-endpoint");
        when(request.getCookies()).thenReturn(
            new Cookie[]{new Cookie("access-token", "invalidToken")});
        doThrow(FeignException.Unauthorized.class).when(authAdapter).validateToken();

        // Act & Assert
        assertThrows(InvalidTokenException.class, () -> {
            validationTokenFilter.doFilterInternal(request, response, filterChain);
        });

        verify(response, times(1)).addCookie(
            argThat(cookie -> cookie.getName().equals("access-token")));
        verify(response, times(1)).addCookie(
            argThat(cookie -> cookie.getName().equals("refresh-token")));
    }

    @Test
    void testFilterWithStaticResource() throws ServletException, IOException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/css/style.css");

        // Act
        validationTokenFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(filterChain, times(1)).doFilter(request, response);
        verifyNoInteractions(authAdapter);
    }
}
