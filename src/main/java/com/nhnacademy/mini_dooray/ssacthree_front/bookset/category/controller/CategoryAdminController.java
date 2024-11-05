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

        model.addAttribute("categories", response.getBody());
        return "admin/category/categoryManagement";
    }

    /**
     * 카테고리 생성
     */
    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        ResponseEntity<List<CategoryInfoResponse>> response = categoryCommonService.getAllCategories();
        List<CategoryInfoResponse> flatCategories = categoryCommonService.flattenCategories(response.getBody()); // 평탄화된 카테고리 리스트 생성

        model.addAttribute("categories", flatCategories); // 모델에 추가하여 뷰로 전달
        return "admin/category/categoryAddOrEdit";
    }

    /**
     * 카테고리 생성 요청
     */
    @PostMapping("/create")
    public String createCategory(CategorySaveRequest request, Model model) {
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
        return "admin/category/categoryAddOrEdit";
    }

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
