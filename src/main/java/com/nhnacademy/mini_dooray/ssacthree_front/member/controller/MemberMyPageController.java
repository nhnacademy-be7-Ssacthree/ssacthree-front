package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.aop.annotation.LoginRequired;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberMyPageController {

    private final MemberService memberService;

    @LoginRequired
    @GetMapping("/my-page")
    public String myPage(Model model, HttpServletRequest request) {

        memberService.getMemberInfo(request);
        model.addAttribute("memberInfo", memberService.getMemberInfo(request));
        return "myPage";
    }

    @LoginRequired
    @PostMapping("/my-page/update")
    public String updateUser(@ModelAttribute MemberInfoUpdateRequest memberInfoUpdateRequest,
        HttpServletRequest request) {
        memberService.memberInfoUpdate(memberInfoUpdateRequest, request);
        return "redirect:/members/my-page";
    }

    @LoginRequired
    @PostMapping("/withdraw")
    public String updateUser(HttpServletRequest request, HttpServletResponse response) {
        memberService.memberWithdraw(request, response);
        return "redirect:/";
    }

}
