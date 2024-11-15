package com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryCommonService {

    ResponseEntity<List<CategoryInfoResponse>> getAllCategories();

    ResponseEntity<CategoryInfoResponse> getCategoryById(Long categoryId);

    ResponseEntity<List<CategoryInfoResponse>> getChildCategories(Long parentCategoryId);

    List<CategoryInfoResponse> getRootCategories();

    ResponseEntity<List<CategoryInfoResponse>> searchCategoriesByName(String name);

    List<CategoryInfoResponse> getCategoryPath(Long categoryId);

    ResponseEntity<List<CategoryInfoResponse>> getCategoryWithChildren(Long categoryId, int depth);

    ResponseEntity<List<CategoryInfoResponse>> getAllDescendants(Long categoryId);

    List<CategoryInfoResponse> flattenCategories(List<CategoryInfoResponse> categories);

}
