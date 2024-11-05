package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.aop.annotation.LoginRequired;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberMyPageController {

    private final MemberService memberService;

    @LoginRequired
    @GetMapping("/myPage")
    public String myPage(Model model, HttpServletRequest request) {

        memberService.getMemberInfo(request);

        model.addAttribute("member");
        model.addAttribute("recentOrders");
        model.addAttribute("wishlist");

        model.addAttribute("coupons");
        model.addAttribute("accountInfo");
        model.addAttribute("addresses");
        return "myPage";
    }
}
