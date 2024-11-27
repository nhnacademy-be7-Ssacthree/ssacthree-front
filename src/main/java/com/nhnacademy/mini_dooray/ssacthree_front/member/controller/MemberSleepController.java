package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sleep-member")
public class MemberSleepController {

    @PostMapping
    public String memberSleep() {
        return null;
    }
}
