package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.TagMgmtService;
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
public class TagMgmtController {

    private final TagMgmtService tagMgmtService;

    @GetMapping
    public String tagView(Model model) {

        model.addAttribute("tagList", tagMgmtService.getAllTags());
        return "admin/tag/Tag";
    }

    @PostMapping
    public String tagCreate(@ModelAttribute TagCreateRequest tagCreateRequest) {
        tagMgmtService.createTag(tagCreateRequest);
        return "redirect:/admin/tags";
    }

}
