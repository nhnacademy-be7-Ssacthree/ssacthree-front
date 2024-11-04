package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
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

    @GetMapping("/myPage")
    public String myPage(Model model) {
        if(!memberService.isAuthenticated()) {
            return "redirect:/";
        }
        model.addAttribute("member");
        model.addAttribute("recentOrders");
        model.addAttribute("wishlist");

        model.addAttribute("coupons");
        model.addAttribute("accountInfo");
        model.addAttribute("addresses");
        return "myPage";
    }
}
