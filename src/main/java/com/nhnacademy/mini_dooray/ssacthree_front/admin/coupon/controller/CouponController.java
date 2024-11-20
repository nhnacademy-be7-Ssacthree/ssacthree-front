package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.service.CouponService;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.service.CouponRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupons")
public class CouponController {

    private final CouponService couponService;
    private final CouponRuleService couponRuleService;

    @GetMapping
    public String coupon(Model model) {
        model.addAttribute("coupons", couponService.getAllCoupons());

        List<CouponRuleGetResponse> couponRules = couponRuleService.getAllSelectedCouponRules();
        model.addAttribute("couponRules", couponRules);

        return "admin/coupon/coupons";
    }

    @PostMapping
    public String updateCoupon(@Valid @ModelAttribute CouponUpdateRequest couponUpdateRequest,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        couponService.updateCoupon(couponUpdateRequest);

        return "redirect:/admin/coupons";
    }

    @GetMapping("/create")
    public String createCoupon(Model model) {
        List<CouponRuleGetResponse> couponRules = couponRuleService.getAllSelectedCouponRules();
        model.addAttribute("couponRules", couponRules);

        return "admin/coupon/createCoupon";
    }

    @PostMapping("/create")
    public String createCoupon(@Valid @ModelAttribute CouponCreateRequest couponCreateRequest,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        couponService.createCoupon(couponCreateRequest);

        return "redirect:/admin/coupons";
    }
}
