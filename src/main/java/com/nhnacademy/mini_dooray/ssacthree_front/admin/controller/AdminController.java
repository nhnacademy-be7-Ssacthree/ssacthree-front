package com.nhnacademy.mini_dooray.ssacthree_front.admin.controller;

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
