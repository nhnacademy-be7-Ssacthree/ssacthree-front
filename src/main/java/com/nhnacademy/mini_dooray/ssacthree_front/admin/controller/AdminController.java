package com.nhnacademy.mini_dooray.ssacthree_front.admin.controller;


import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.AdminLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.service.AdminService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    @GetMapping("/admin")
    public String admin(HttpServletRequest request) {

        //구현 고민 중 ㅠ
        if (CookieUtil.checkAccessTokenCookie(request)) {
            return "admin/admin";
        }
        return "redirect:/admin-login";

    }

    @GetMapping("/admin-login")
    public String adminLogin() {
        return "admin/adminLogin";
    }

    @PostMapping("/admin-login")
    public String adminLoginPost(HttpServletResponse response,
        @ModelAttribute AdminLoginRequest adminLoginRequest) {
        adminService.login(response, adminLoginRequest);
        return "redirect:/admin";
    }

}
