package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/adminMainPage")
    public String adminMain() {
        return "adminMainPage";
    }
}
