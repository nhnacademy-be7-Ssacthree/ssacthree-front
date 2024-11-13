package com.nhnacademy.mini_dooray.ssacthree_front.admin.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.AdminLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import jakarta.servlet.http.HttpServletResponse;

public interface AdminService {

    MessageResponse login(HttpServletResponse httpServletResponse,
        AdminLoginRequest adminLoginRequest);
}
