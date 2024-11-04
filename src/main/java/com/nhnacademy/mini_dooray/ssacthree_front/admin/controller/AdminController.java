package com.nhnacademy.mini_dooray.ssacthree_front.admin.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.service.AdminService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.exception.ValidationFailedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String admin() {
        return "admin/admin";
    }

    @GetMapping("/main")
    public String adminMain() {
        return "admin/adminMainPage";
    }

    @GetMapping("/deliveryRules")
    public String deliveryRule(Model model) {
        model.addAttribute("deliveryRules", adminService.getAllDeliveryRules());
        return "admin/deliveryRule/deliveryRules";
    }

    @GetMapping("/deliveryRules/create")
    public String createDeliveryRule() {
        return "admin/deliveryRule/createDeliveryRule";
    }

    @PostMapping("/deliveryRules/create")
    public String createDeliveryRule(@Valid @ModelAttribute DeliveryRuleCreateRequest deliveryRuleCreateRequest,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        adminService.createDeliveryRule(deliveryRuleCreateRequest);

        return "redirect:/admin/deliveryRules";
    }

    @PutMapping("/deliveryRules")
    public String updateDeliveryRule(@Valid @ModelAttribute DeliveryRuleUpdateRequest deliveryRuleUpdateRequest,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        adminService.updateDeliveryRule(deliveryRuleUpdateRequest);

        return "redirect:/admin/deliveryRules";
    }
}
