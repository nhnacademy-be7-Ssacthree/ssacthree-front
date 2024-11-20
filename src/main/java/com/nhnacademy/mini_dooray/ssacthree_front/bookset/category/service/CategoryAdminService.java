package com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryAdminService {

    ResponseEntity<CategoryInfoResponse> createCategory(CategorySaveRequest request);

    ResponseEntity<CategoryInfoResponse> updateCategory(Long categoryId, CategoryUpdateRequest request);

    ResponseEntity<Boolean> deleteCategory(Long categoryId);

    ResponseEntity<List<CategoryInfoResponse>> getAllCategoriesForAdmin();

}
