package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.PointSaveRuleService;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/point-save-rules")
public class PointSaveRuleController {

    private final PointSaveRuleService pointSaveRuleService;

    @GetMapping
    public String pointSaveRule(Model model) {
        model.addAttribute("pointSaveRules", pointSaveRuleService.getAllPointSaveRules());
        return "admin/pointSaveRule/pointSaveRules";
    }

    @PostMapping
    public String updatePointSaveRule(@Valid @ModelAttribute PointSaveRuleUpdateRequest pointSaveRuleUpdateRequest,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        pointSaveRuleService.updatePointSaveRule(pointSaveRuleUpdateRequest);

        return "redirect:/admin/point-save-rules";
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

        return "redirect:/admin/point-save-rules";
    }
}
