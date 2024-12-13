package com.nhnacademy.mini_dooray.ssacthree_front.commons.filter;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.adapter.AuthAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

class ReissueFilterTest {

    private AuthAdapter adapter;
    private ReissueFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    private static final String SET_COOKIE_HEADER = "Set-Cookie";

    @BeforeEach
    public void setUp() {
        adapter = mock(AuthAdapter.class);
        filter = new ReissueFilter(adapter);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @Test
    void testAccessTokenNull_RefreshTokenNotNull() throws IOException, ServletException {
        // Arrange
        Cookie refreshTokenCookie = new Cookie("refresh-token", "some-refresh-token");
        when(request.getCookies()).thenReturn(new Cookie[]{refreshTokenCookie});
        when(request.getRequestURI()).thenReturn("/someUri");

        // Mock the adapter response
        HttpHeaders headers = new HttpHeaders();
        headers.add(SET_COOKIE_HEADER, "access-token=some-access-token; Path=/; HttpOnly");
        headers.add(SET_COOKIE_HEADER, "refresh-token=new-refresh-token; Path=/; HttpOnly");
        ResponseEntity<MessageResponse> mockResponse = ResponseEntity.ok().headers(headers)
            .body(new MessageResponse());

        when(adapter.reissueToken()).thenReturn(mockResponse);

        // Act
        filter.doFilterInternal(request, response, chain);

        // Assert
        verify(adapter, times(1)).reissueToken();
        verify(response, times(2)).addHeader(eq(SET_COOKIE_HEADER), anyString());
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testExceptionOccurs_RedirectsToRoot() throws IOException, ServletException {
        // Arrange
        Cookie refreshTokenCookie = new Cookie("refresh-token", "some-refresh-token");
        when(request.getCookies()).thenReturn(new Cookie[]{refreshTokenCookie});
        when(request.getRequestURI()).thenReturn("/someUri");

        // Mock adapter to throw an exception
        when(adapter.reissueToken()).thenThrow(new RuntimeException("Token reissue failed"));

        // Act
        filter.doFilterInternal(request, response, chain);

        // Assert
        verify(response, times(1)).sendRedirect("/");
        verify(chain, never()).doFilter(request, response);
    }

    @Test
    void testNoTokensPresent() throws IOException, ServletException {
        // Arrange
        when(request.getCookies()).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/someUri");

        // Act
        filter.doFilterInternal(request, response, chain);

        // Assert
        verify(adapter, never()).reissueToken();
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void testStaticResourcesAreExcluded() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/images/logo.png");

        // Act
        filter.doFilterInternal(request, response, chain);

        // Assert
        verify(chain, times(1)).doFilter(request, response);
        verifyNoInteractions(adapter);
    }

    @Test
    void testAccessTokenPresent() throws IOException, ServletException {
        // Arrange
        Cookie accessTokenCookie = new Cookie("access-token", "some-access-token");
        when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});
        when(request.getRequestURI()).thenReturn("/someUri");

        // Act
        filter.doFilterInternal(request, response, chain);

        // Assert
        verify(adapter, never()).reissueToken();
        verify(chain, times(1)).doFilter(request, response);
    }
}
