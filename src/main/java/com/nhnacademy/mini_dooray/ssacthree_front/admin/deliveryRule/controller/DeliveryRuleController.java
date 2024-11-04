package com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.service.DeliveryRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.exception.ValidationFailedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/deliveryRules")
public class DeliveryRuleController {

    private final DeliveryRuleService adminService;

    @GetMapping
    public String deliveryRule(Model model) {
        model.addAttribute("deliveryRules", adminService.getAllDeliveryRules());
        return "admin/deliveryRule/deliveryRules";
    }

    @PutMapping
    public String updateDeliveryRule(@Valid @ModelAttribute DeliveryRuleUpdateRequest deliveryRuleUpdateRequest,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        adminService.updateDeliveryRule(deliveryRuleUpdateRequest);

        return "redirect:/admin/deliveryRules";
    }

    @GetMapping("/create")
    public String createDeliveryRule() {
        return "admin/deliveryRule/createDeliveryRule";
    }

    @PostMapping("/create")
    public String createDeliveryRule(@Valid @ModelAttribute DeliveryRuleCreateRequest deliveryRuleCreateRequest,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        adminService.createDeliveryRule(deliveryRuleCreateRequest);

        return "redirect:/admin/deliveryRules";
    }
}
