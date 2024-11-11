package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service.PackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/packaging")
public class PackagingController {

    private final PackagingService packagingService;

    @GetMapping
    public String packagingView(Model model) {
        model.addAttribute("packagingList", packagingService.getAllPackaging());
        return "admin/packaging/packaging";
    }


}
