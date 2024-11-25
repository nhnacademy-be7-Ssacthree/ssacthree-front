package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberMyPageController {

    private final MemberService memberService;


    @GetMapping("/my-page")
    public String myPage(Model model, HttpServletRequest request) {

        model.addAttribute("memberInfo", memberService.getMemberInfo(request));
        return "myPage";
    }


    @PostMapping("/my-page/update")
    public String updateUser(@Valid @ModelAttribute MemberInfoUpdateRequest memberInfoUpdateRequest,
        BindingResult bindingResult,
        HttpServletRequest request, RedirectAttributes redirectAttributes) {
        try {
            if (bindingResult.hasErrors()) {
                throw new ValidationFailedException(bindingResult);
            }
            // 휴대폰 번호 패턴만 변경..
            memberInfoUpdateRequest.setCustomerPhoneNumber(
                memberInfoUpdateRequest.getCustomerPhoneNumber()
                    .replaceAll("(\\d{3})(\\d{4})(\\d{4})",
                        "$1-$2-$3"));
            memberService.memberInfoUpdate(memberInfoUpdateRequest, request);
            return "redirect:/members/my-page";
        } catch (ValidationFailedException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/members/my-page";
        }

    }


    @PostMapping("/withdraw")
    public String deleteUser(HttpServletRequest request, HttpServletResponse response) {
        memberService.memberWithdraw(request, response);
        return "redirect:/";
    }

}
