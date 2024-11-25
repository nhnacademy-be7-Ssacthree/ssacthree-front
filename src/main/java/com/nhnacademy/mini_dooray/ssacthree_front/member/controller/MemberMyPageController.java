package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;


import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.InvalidTokenException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.CookieUtil;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private final BookCommonService bookCommonService;
    private final String memberUrl;

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

    @GetMapping("/my-page/likes")
    public String showMyLikeBooks(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "12") int size,
                                  @RequestParam(defaultValue = "bookName:asc") String[] sort,
                                  Model model, HttpServletRequest request) {
        if (!CookieUtil.checkAccessTokenCookie(request)) {
            throw new InvalidTokenException("유효하지 않은 토큰");
        }
        List<Long> likeBooks = bookCommonService.getLikedBooksIdForCurrentUser();
        model.addAttribute("likeBooks", likeBooks);
        model.addAttribute("memberUrl", memberUrl);

        // 공통 필터 파라미터를 Map으로 정리
        Map<String, Object> allParams = new HashMap<>();
        allParams.put("page", page);
        allParams.put("size", size);
        allParams.put("sort", sort);

        Page<BookListResponse> books = bookCommonService.getBooksByMemberId(page, size, sort);

        // `sort`를 제외한 추가 파라미터 문자열 생성
        String extraParams = allParams.entrySet().stream()
                .filter(entry -> !"page".equals(entry.getKey()) && !"size".equals(entry.getKey()) && !"sort".equals(entry.getKey()))
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        model.addAttribute("books", books);
        model.addAttribute("baseUrl", "/members/my-page/likes");
        model.addAttribute("extraParams", extraParams.isEmpty() ? "" : "&" + extraParams); // 항상 &로 시작
        model.addAttribute("sort", sort[0]); // 현재 선택된 정렬 방식 추가
        return "myLikes";
    }

}
