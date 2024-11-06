package com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.service.PointSaveRuleService;

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
@RequestMapping("/admin/pointSaveRules")
public class PointSaveRuleController {

    private final PointSaveRuleService pointSaveRuleService;

    @GetMapping
    public String pointSaveRule(Model model) {
        model.addAttribute("pointSaveRules", pointSaveRuleService.getAllPointSaveRules());
        return "admin/pointSaveRule/pointSaveRules";
    }

    @GetMapping("/create")
    public String createPointSaveRule() {
        return "admin/pointSaveRule/createPointSaveRule";
    }

    @PostMapping("/create")
    public String createPointSaveRule(@Valid @ModelAttribute PointSaveRuleCreateRequest pointSaveRuleCreateRequest,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        pointSaveRuleService.createPointSaveRule(pointSaveRuleCreateRequest);

        return "redirect:/admin/pointSaveRules";
    }
}
