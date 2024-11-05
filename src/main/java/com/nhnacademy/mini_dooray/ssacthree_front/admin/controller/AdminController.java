package com.nhnacademy.mini_dooray.ssacthree_front.admin.controller;


import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.service.AdminService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    @GetMapping
    public String admin() {
        return "admin/admin";
    }

    @GetMapping("/main")
    public String adminMain() {
        return "admin/adminMainPage";
    }
}
