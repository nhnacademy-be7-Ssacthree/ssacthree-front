package com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import org.springframework.http.ResponseEntity;

public interface CategoryAdminService {

    ResponseEntity<CategoryInfoResponse> createCategory(CategorySaveRequest request);

    ResponseEntity<CategoryInfoResponse> updateCategory(Long categoryId, CategoryUpdateRequest request);

    ResponseEntity<Boolean> deleteCategory(Long categoryId);

}
