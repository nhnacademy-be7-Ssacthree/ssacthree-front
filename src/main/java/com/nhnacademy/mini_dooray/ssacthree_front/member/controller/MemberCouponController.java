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
                         @RequestParam(defaultValue = "memberCouponCreatedAt:asc") String[] sort) {

        Map<String, Object> allParams = new HashMap<>();
        allParams.put("page", page);
        allParams.put("size", size);
        allParams.put("sort", sort);

        Page<MemberCouponGetResponse> notUsedMemberCoupons = memberCouponService.getNotUsedMemberCoupons(page, size, sort);
        Page<MemberCouponGetResponse> usedMemberCoupons = memberCouponService.getUsedMemberCoupons(page, size, sort);

        String extraParams = allParams.entrySet().stream()
                .filter(entry -> !"page".equals(entry.getKey()) && !"size".equals(entry.getKey()) && !"sort".equals(entry.getKey()))
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        model.addAttribute("notUsedMemberCoupons", notUsedMemberCoupons);
        model.addAttribute("usedMemberCoupons", usedMemberCoupons);
        model.addAttribute("baseUrl", "/coupons");
        model.addAttribute("extraParams", extraParams.isEmpty() ? "" : "&" + extraParams);
        model.addAttribute("sort", sort[0]);

        return "memberCoupon";
    }
}
