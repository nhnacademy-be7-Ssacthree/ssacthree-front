package com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.adapter.CategoryAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final CategoryAdapter categoryAdapter;

    public CategoryAdminServiceImpl(CategoryAdapter categoryAdapter) {
        this.categoryAdapter = categoryAdapter;
    }

    @Override
    public ResponseEntity<CategoryInfoResponse> createCategory(CategorySaveRequest request) {
        return categoryAdapter.createCategory(request);
    }

    @Override
    public ResponseEntity<CategoryInfoResponse> updateCategory(Long categoryId, CategoryUpdateRequest request) {
        return categoryAdapter.updateCategory(categoryId, request);
    }

    @Override
    public ResponseEntity<Boolean> deleteCategory(Long categoryId) {
        return categoryAdapter.deleteCategory(categoryId);
    }

    @Override
    public ResponseEntity<List<CategoryInfoResponse>> getAllCategoriesForAdmin() {
        return categoryAdapter.getAllCategoriesForAdmin();
    }


}
