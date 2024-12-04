package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service.PackagingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/packaging")
public class PackagingController {

    private final PackagingService packagingService;

    private static final String REDIRECT_TO_PACKAGING = "redirect:/admin/packaging";

    @GetMapping
    public String packagingView(Model model) {
        model.addAttribute("packagingList", packagingService.getAllPackaging());
        return "admin/packaging/packaging";
    }

    @PostMapping
    public String createPackaging(@RequestParam("name") String name,
                                  @RequestParam("price") int price,
                                  @RequestParam("imageUrl") MultipartFile imageFile) {
        String imageUrl = packagingService.uploadImage(imageFile);

        // DTO 생성 시 생성자를 이용하는 방법
        PackagingCreateRequest packagingCreateRequest = new PackagingCreateRequest(name, price, imageUrl);

        packagingService.createPackaging(packagingCreateRequest);
        return REDIRECT_TO_PACKAGING;
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

        return REDIRECT_TO_PACKAGING;
    }

    @PostMapping("/delete/{packaging-id}")
    public String deletePackaging(@PathVariable("packaging-id") Long id) {
        packagingService.deletePackaging(id);
        return REDIRECT_TO_PACKAGING;
    }
}
