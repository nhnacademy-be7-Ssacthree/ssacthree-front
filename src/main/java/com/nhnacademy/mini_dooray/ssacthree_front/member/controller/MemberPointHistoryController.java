package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PointHistoryGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.PointHistoryService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/point-histories")
@RequiredArgsConstructor
public class MemberPointHistoryController {

    private final PointHistoryService pointHistoryService;
    private final MemberService memberService;

    @GetMapping
    public String pointHistory(Model model, HttpServletRequest request,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "pointChangeDate") String sort,
        @RequestParam(defaultValue = "DESC") String direction) {

        Page<PointHistoryGetResponse> pointHistories = pointHistoryService.getPointHistory(page,
            size, sort, direction);
        model.addAttribute("member", memberService.getMemberInfo(request));
        Map<String, Object> allParams = new HashMap<>();
        allParams.put("page", String.valueOf(page));
        allParams.put("size", String.valueOf(size));

        String extraParams = allParams.entrySet().stream()
            .filter(entry -> !"page".equals(entry.getKey()) && !"size".equals(entry.getKey()))
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));
        model.addAttribute("pointHistories", pointHistories);
        model.addAttribute("baseUrl", "/point-histories");
        model.addAttribute("allParams", allParams);
        model.addAttribute("extraParams", extraParams);
        return "memberPointHistory";
    }
}
