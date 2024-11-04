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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String admin() {
        return "admin";
    }

    @GetMapping("/main")
    public String adminMain() {
        return "adminMainPage";
    }

    @GetMapping("/deliveryRules")
    public String deliveryRule(Model model) {
        model.addAttribute("deliveryRules", adminService.getAllDeliveryRules());
        return "deliveryRules";
    }

    @GetMapping("/deliveryRules/create")
    public String createDeliveryRule() {
        return "createDeliveryRule";
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

    @PostMapping("/deliveryRules/update")
    public String updateDeliveryRule(@Valid @ModelAttribute DeliveryRuleUpdateRequest deliveryRuleUpdateRequest,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        adminService.updateDeliveryRule(deliveryRuleUpdateRequest);

        return "redirect:/admin/deliveryRules";
    }
}
