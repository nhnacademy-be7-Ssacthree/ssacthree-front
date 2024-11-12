package com.nhnacademy.mini_dooray.ssacthree_front.admin.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.adapter.AdminAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.AdminLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.service.AdminService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.CookieUtil;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminAdapter adminAdapter;

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
}
