package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.MemberRegisterFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


/**
 * 회원가입을 처리하는 컨트롤러
 *
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
        BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {

        try {
            if (bindingResult.hasErrors()) {
                throw new ValidationFailedException(bindingResult);
            }
            // 휴대폰 번호 패턴만 변경..
            memberRegisterRequest.setCustomerPhoneNumber(
                memberRegisterRequest.getCustomerPhoneNumber()
                    .replaceAll("(\\d{3})(\\d{4})(\\d{4})",
                        "$1-$2-$3"));
            memberService.memberRegister(memberRegisterRequest);
            redirectAttributes.addFlashAttribute("registered", "환영합니다!");
            return "redirect:/";
        } catch (ValidationFailedException | MemberRegisterFailedException e) {
            model.addAttribute("error", e.getMessage());
            return "memberRegister";
        }


    }
}
