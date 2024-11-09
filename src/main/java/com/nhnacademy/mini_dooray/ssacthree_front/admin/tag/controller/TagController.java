package com.nhnacademy.mini_dooray.ssacthree_front.admin.tag.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public String tagView(Model model) {

        model.addAttribute("tagList", tagService.getAllTags());
        return "admin/tag/Tag";
    }

    @PostMapping
    public String tagCreate(@ModelAttribute TagCreateRequest tagCreateRequest) {
        tagService.createTag(tagCreateRequest);
        return "redirect:/admin/tags";
    }

}
