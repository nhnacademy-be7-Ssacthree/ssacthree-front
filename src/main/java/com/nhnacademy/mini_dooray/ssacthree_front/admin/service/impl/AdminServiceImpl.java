package com.nhnacademy.mini_dooray.ssacthree_front.admin.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.adapter.AdminAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.AdminLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.service.AdminService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.CookieUtil;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LogoutIllegalAccessException;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminAdapter adminAdapter;
    private static final String SET_COOKIE = "Set-Cookie";
    
    @Override
    public MessageResponse login(HttpServletResponse httpServletResponse,
        AdminLoginRequest adminLoginRequest) {

        try {
            ResponseEntity<MessageResponse> response = adminAdapter.adminLogin(adminLoginRequest);
            if (response.getStatusCode().is2xxSuccessful()) {
                CookieUtil.addCookieFromFeignClient(httpServletResponse, response);
                return response.getBody();
            }
            throw new RuntimeException("어드민 로그인 불가능");
        } catch (FeignException e) {
            throw new RuntimeException("어드민 로그인 불가능");
        }
    }

    @Override
    public MessageResponse logout(HttpServletResponse httpServletResponse) {
        ResponseEntity<MessageResponse> response = adminAdapter.logout();

        try {

            if (isHaveCookie(httpServletResponse, response)) {
                return response.getBody();
            }

            throw new LogoutIllegalAccessException("잘못된 접근입니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new LogoutIllegalAccessException("잘못된 접근입니다.");
        }
    }

    private boolean isHaveCookie(HttpServletResponse httpServletResponse,
        ResponseEntity<MessageResponse> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            List<String> cookies = response.getHeaders().get(SET_COOKIE);
            assert cookies != null;
            httpServletResponse.addHeader(SET_COOKIE, cookies.get(0));
            httpServletResponse.addHeader(SET_COOKIE, cookies.get(1));
            return true;
        }
        return false;
    }
}
