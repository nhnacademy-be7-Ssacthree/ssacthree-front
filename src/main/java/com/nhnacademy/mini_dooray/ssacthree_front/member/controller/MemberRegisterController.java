package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberRegisterController {

    @GetMapping("/register")
    public String memberRegister() {
        return "memberRegister";
    }

    @PostMapping("/register")
    public String memberRegister(@ModelAttribute MemberRegisterRequest memberRegisterRequest, Model model) {

    }
}
