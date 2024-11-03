package com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryAdminService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryCommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class CategoryAdminController {
    private final CategoryCommonService categoryCommonService;
    private final CategoryAdminService categoryAdminService;

    public CategoryAdminController(CategoryCommonService categoryCommonService, CategoryAdminService categoryAdminService) {
        this.categoryCommonService = categoryCommonService;
        this.categoryAdminService = categoryAdminService;
    }

    /**
     * 카테고리 관리 페이지 - 모든 카테고리 조회
     */
    @GetMapping
    public String viewAdminCategories(Model model) {
        ResponseEntity<List<CategoryInfoResponse>> response = categoryCommonService.getAllCategories();

        List<CategoryInfoResponse> categories = response.getBody();

        // todo: 디버깅용: 각 카테고리의 children 필드를 출력
        for (CategoryInfoResponse category : categories) {
            System.out.println("Category: " + category.getCategoryName());
            System.out.println("Children: " + (category.getChildren() != null ? category.getChildren() : "null"));
            String childName = category.getChildren() != null && !category.getChildren().isEmpty() ? category.getChildren().get(0).getCategoryName() : "";
            System.out.println("Child Name: " + childName);
        }

        model.addAttribute("categories", response.getBody());
        return "categoryManagement";
    }

    /**
     * 카테고리 생성
     */
    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        ResponseEntity<List<CategoryInfoResponse>> response = categoryCommonService.getAllCategories();
        List<CategoryInfoResponse> flatCategories = categoryCommonService.flattenCategories(response.getBody()); // 평탄화된 카테고리 리스트 생성

        // todo: 디버깅용: 평탄화된 카테고리 리스트의 children 필드를 출력
        for (CategoryInfoResponse category : flatCategories) {
            System.out.println("Flat Category: " + category.getCategoryName());
            System.out.println("Children: " + category.getChildren());
        }

        model.addAttribute("categories", flatCategories); // 모델에 추가하여 뷰로 전달
        return "categoryAddOrEdit";
    }

    /**
     * 카테고리 생성 요청
     */
    @PostMapping("/create")
    public String createCategory(CategorySaveRequest request, Model model) {
        // todo: 디버깅용
        System.out.println("Category Name: " + request.getCategoryName());
        System.out.println("Super Category ID: " + request.getSuperCategoryId());

        categoryAdminService.createCategory(request);
        return "redirect:/admin/categories";
    }

    /**
     * 카테고리 수정
     */
    @GetMapping("/update/{categoryId}")
    public String updateCategoryForm(@PathVariable Long categoryId, Model model) {
        CategoryInfoResponse category = categoryCommonService.getCategoryById(categoryId).getBody();
        model.addAttribute("category", category);
        return "categoryAddOrEdit";
    }

    //todo: html에서는 post와 get만 지원한다고 하여 수정과 삭제를 일단 post로 했는데,
    // put이나 delete를 쓸 수 있긴 하지만 html에서 메소드 처리가 필요하다고 함.
    // 어떻게 할까요?
    /**
     * 카테고리 수정 요청
     */
    @PostMapping("/update/{categoryId}")
    public String updateCategory(@PathVariable Long categoryId, CategoryUpdateRequest request, Model model) {
        categoryAdminService.updateCategory(categoryId, request);
        return "redirect:/admin/categories";
    }

    /**
     * 카테고리 삭제 요청
     */
    @PostMapping("/delete/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId, Model model) {
        categoryAdminService.deleteCategory(categoryId);
        return "redirect:/admin/categories";
    }

    /**
     * 카테고리 ID로 카테고리 정보 요청
     */
    @GetMapping("/admin/categories/{categoryId}")
    public ResponseEntity<CategoryInfoResponse> getCategoryById(@PathVariable Long categoryId) {
        CategoryInfoResponse categoryInfo = categoryCommonService.getCategoryById(categoryId).getBody(); // 서비스에서 카테고리 정보를 가져옴
        return ResponseEntity.ok(categoryInfo);
    }


}
