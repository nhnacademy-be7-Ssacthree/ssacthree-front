package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.exception.ValidationFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


/**
 * 회원가입을 처리하는 컨트롤러
 * @author : 김희망
 * @date : 2024/11/03
 */
@Controller
@RequiredArgsConstructor
public class MemberRegisterController {

    private final MemberService memberService;

    @GetMapping("/register")
    public String memberRegister() {
        return "memberRegister";
    }

    @PostMapping("/register")
    public String memberRegister(@Valid @ModelAttribute MemberRegisterRequest memberRegisterRequest,
        BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        memberService.memberRegister(memberRegisterRequest);
        return "redirect:/";
    }
}
