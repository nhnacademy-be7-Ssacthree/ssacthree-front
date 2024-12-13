package com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.PagingInfo;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service.DeliveryRuleService;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/delivery-rules")
public class DeliveryRuleController {

    private final DeliveryRuleService deliveryRuleService;

    @GetMapping
    public String deliveryRule(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size
                                ,Model model) {

        List<DeliveryRuleGetResponse> allRules = deliveryRuleService.getAllDeliveryRules();
        log.info("데이터를받아왔습니다. {}", allRules);

        // 전체 데이터 크기 계산
        int totalRules = allRules.size();
        int totalPages = (int) Math.ceil((double) totalRules / size);
        // 페이지 범위 계산
        int start = page * size;
        int end = Math.min(start + size, totalRules);
        log.info("totalRules: {} / totalPages: {}", totalRules, totalPages);
        log.info("start: {} / end: {}", start, end);

        // 요청한 페이지 범위에 데이터가 없는 경우 빈 리스트 반환
        List<DeliveryRuleGetResponse> pagedRules = start >= totalRules ? List.of() : allRules.subList(start, end);

        log.info("pagedRules: {}", pagedRules);
        PagingInfo paging = new PagingInfo(page, size, totalPages, totalRules, null);

        model.addAttribute("deliveryRules", pagedRules); // 현재 페이지 데이터
        model.addAttribute("paging", paging);
        model.addAttribute("baseUrl", "/admin/delivery-rules");
        model.addAttribute("extraParams", ""); // 따로 정렬하지 않음

        return "admin/deliveryRule/deliveryRules";
    }

    @PostMapping
    public String updateDeliveryRule(@Valid @ModelAttribute DeliveryRuleUpdateRequest deliveryRuleUpdateRequest,
                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        deliveryRuleService.updateDeliveryRule(deliveryRuleUpdateRequest);

        return "redirect:/admin/delivery-rules";
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

        deliveryRuleService.createDeliveryRule(deliveryRuleCreateRequest);

        return "redirect:/admin/delivery-rules";
    }
}
