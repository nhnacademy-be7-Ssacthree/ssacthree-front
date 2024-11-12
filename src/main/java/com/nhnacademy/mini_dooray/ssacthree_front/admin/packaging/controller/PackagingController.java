package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service.PackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public String createPackaging(@ModelAttribute PackagingCreateRequest packagingCreateRequest) {
        packagingService.createPackaging(packagingCreateRequest);
        return "redirect:/admin/packaging";
    }

    @PostMapping("/update/{packaging-id}")
    public String updatePackaging(@PathVariable("packaging-id") Long packagingId,
                                  @RequestParam("packagingName") String packagingName,
                                  @RequestParam("packagingPrice") int packagingPrice,
                                  @RequestParam("packagingImageUrl") String packagingImageUrl) {

        // PackagingUpdateRequest 객체 생성
        PackagingUpdateRequest packagingUpdateRequest = new PackagingUpdateRequest(packagingName, packagingPrice, packagingImageUrl);

        // 서비스 호출하여 업데이트 처리
        packagingService.updatePackaging(packagingId, packagingUpdateRequest);

        return "redirect:/admin/packaging";
    }

    @PostMapping("/delete/{packaging-id}")
    public String deletePackaging(@PathVariable("packaging-id") Long id) {
        packagingService.deletePackaging(id);
        return "redirect:/admin/packaging";
    }
}
