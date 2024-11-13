package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.dto.CouponRuleUpdateRequest;
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

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/coupon-rules")
public class CouponRuleController {

    private final CouponRuleService couponRuleService;

    @GetMapping
    public String couponRule(Model model) {
        model.addAttribute("couponRules", couponRuleService.getAllCouponRules());
        return "admin/couponRule/couponRules";
    }

    @PostMapping
    public String updateCouponRule(@Valid @ModelAttribute CouponRuleUpdateRequest couponRuleUpdateRequest,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        couponRuleService.updateCouponRule(couponRuleUpdateRequest);

        return "redirect:/admin/coupon-rules";
    }

    @GetMapping("/create")
    public String createCouponRule() {
        return "admin/couponRule/createCouponRule";
    }

    @PostMapping("/create")
    public String createCouponRule(@Valid @ModelAttribute CouponRuleCreateRequest couponRuleCreateRequest,
                                   BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        couponRuleService.createCouponRule(couponRuleCreateRequest);

        return "redirect:/admin/coupon-rules";
    }
}
