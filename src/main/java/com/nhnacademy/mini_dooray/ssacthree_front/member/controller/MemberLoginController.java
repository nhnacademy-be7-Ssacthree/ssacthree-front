package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * 로그인과 로그아웃에 관련된 컨트롤러
 * @author : 김희망
 * @Datge : 2024/11/03
 */
@Controller
@RequiredArgsConstructor
public class MemberLoginController {

    private final MemberService memberService;

    @PostMapping("/login")
    public String login(@ModelAttribute MemberLoginRequest requestBody, Model model, HttpServletResponse httpServletResponse) {
        memberService.memberLogin(requestBody,httpServletResponse);
        return "redirect:/";
    }


    @PostMapping("/logout")
    public String logout(HttpServletResponse httpServletResponse) {
        memberService.memberLogout(httpServletResponse);
        return "redirect:/";
    }


}
