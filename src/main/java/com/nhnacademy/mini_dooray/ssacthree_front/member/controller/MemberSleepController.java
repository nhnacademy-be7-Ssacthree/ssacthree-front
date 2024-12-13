package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberSleepToActiveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class MemberSleepController {

    private final MemberService memberService;

    @PostMapping("/sleep-member")
    public String sleepMemberChangeActive(
        @ModelAttribute MemberSleepToActiveRequest memberSleepToActiveRequest,
        RedirectAttributes redirectAttributes) {

        MessageResponse message = memberService.memberSleepToActive(memberSleepToActiveRequest);
        redirectAttributes.addFlashAttribute("memberActiveMessage", message.getMessage());
        return "redirect:/";
    }

}

