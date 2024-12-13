package com.nhnacademy.mini_dooray.ssacthree_front.commons.util;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;

public class CookieUtil {

    private CookieUtil() {

    }

    public static boolean checkAccessTokenCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return false;
        }

        String accessToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access-token")) {
                accessToken = cookie.getValue();
                break;
            }
        }

        return accessToken != null;
    }

    public static void addCookieFromFeignClient(HttpServletResponse httpServletResponse,
        ResponseEntity<?> response) {
        List<String> cookies = response.getHeaders().get(SET_COOKIE);
        assert cookies != null;
        httpServletResponse.addHeader(SET_COOKIE, cookies.getFirst());
        httpServletResponse.addHeader(SET_COOKIE, cookies.get(1));
    }

    public static Cookie cookieDestroyer(String cookieName) {

        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
