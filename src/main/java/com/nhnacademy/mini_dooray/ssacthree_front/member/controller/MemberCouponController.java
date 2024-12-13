package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberCouponGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberCouponService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/members/my-page/coupons")
@RequiredArgsConstructor
public class MemberCouponController {

    private final MemberCouponService memberCouponService;

    @GetMapping
    public String coupon(Model model, HttpServletRequest request,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "5") int size,
                         @RequestParam(defaultValue = "couponIssueDate") String sort,
                         @RequestParam(defaultValue = "ASC") String direction) {

        Page<MemberCouponGetResponse> memberCoupons = memberCouponService.getMemberCoupons(page,
                size, sort, direction);

        Map<String, Object> allParams = new HashMap<>();
        allParams.put("page", String.valueOf(page));
        allParams.put("size", String.valueOf(size));

        String extraParams = allParams.entrySet().stream()
                .filter(entry -> !"page".equals(entry.getKey()) && !"size".equals(entry.getKey()))
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        model.addAttribute("memberCoupons", memberCoupons);
        model.addAttribute("baseUrl", "/coupons");
        model.addAttribute("allParams", allParams);
        model.addAttribute("extraParams", extraParams);

        return "memberCoupon";
    }
}
