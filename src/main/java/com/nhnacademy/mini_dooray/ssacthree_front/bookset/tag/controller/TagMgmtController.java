package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.TagMgmtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
public class TagMgmtController {

    private final TagMgmtService tagMgmtService;

    @GetMapping
    public String tagView(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookName:asc") String[] sort,
            Model model) {

        Map<String, Object> allParams = new HashMap<>();
        allParams.put("page", String.valueOf(page));
        allParams.put("size", String.valueOf(size));

        String extraParams = allParams.entrySet().stream()
                .filter(entry -> !"page".equals(entry.getKey()) && !"size".equals(entry.getKey()))
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        model.addAttribute("baseUrl", "/admin/tags");
        model.addAttribute("allParams", allParams);
        model.addAttribute("extraParams", extraParams);

        model.addAttribute("tagList", tagMgmtService.getAllTags(page, size, sort));
        return "admin/tag/Tag";
    }

    @PostMapping
    public String tagCreate(@ModelAttribute TagCreateRequest tagCreateRequest) {
        tagMgmtService.createTag(tagCreateRequest);
        return "redirect:/admin/tags";
    }

}
