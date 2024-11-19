package com.nhnacademy.mini_dooray.ssacthree_front.admin.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.AdminLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "adminClient")
public interface AdminAdapter {

    @PostMapping("/auth/admin-login")
    ResponseEntity<MessageResponse> adminLogin(AdminLoginRequest adminLoginRequest);

    @PostMapping("/auth/logout")
    ResponseEntity<MessageResponse> logout();
}
