package com.nhnacademy.mini_dooray.ssacthree_front.commons.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import feign.RequestTemplate;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

class FeignCookieInterceptorTest {

    private FeignCookieInterceptor feignCookieInterceptor;
    private RequestTemplate requestTemplate;

    @BeforeEach
    void setUp() {
        feignCookieInterceptor = new FeignCookieInterceptor();
        requestTemplate = new RequestTemplate();
    }

    @Test
    void apply_shouldAddCookiesToRequestTemplate() {
        // Mock HttpServletRequest and Cookies
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        Cookie[] mockCookies = new Cookie[]{
            new Cookie("SESSION", "abc123"),
            new Cookie("XSRF-TOKEN", "token123")
        };
        when(mockRequest.getCookies()).thenReturn(mockCookies);

        // Mock RequestAttributes
        RequestAttributes mockRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(mockRequestAttributes);

        // Apply the interceptor
        feignCookieInterceptor.apply(requestTemplate);

        // Verify the "Cookie" header in the RequestTemplate
        String expectedHeader = "SESSION=abc123;XSRF-TOKEN=token123;";
        assertEquals(expectedHeader, requestTemplate.headers().get("Cookie").iterator().next());
    }

    @Test
    void apply_shouldNotFailWhenNoCookies() {
        // Mock HttpServletRequest without cookies
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(mockRequest.getCookies()).thenReturn(null);

        // Mock RequestAttributes
        RequestAttributes mockRequestAttributes = new ServletRequestAttributes(mockRequest);
        RequestContextHolder.setRequestAttributes(mockRequestAttributes);

        // Apply the interceptor
        feignCookieInterceptor.apply(requestTemplate);

        // Verify the "Cookie" header is not present
        assertEquals(false, requestTemplate.headers().containsKey("Cookie"));
    }

    @Test
    void apply_shouldNotFailWhenNoRequestAttributes() {
        // Clear RequestContextHolder
        RequestContextHolder.resetRequestAttributes();

        // Apply the interceptor
        feignCookieInterceptor.apply(requestTemplate);

        // Verify the "Cookie" header is not present
        assertEquals(false, requestTemplate.headers().containsKey("Cookie"));
    }
}
