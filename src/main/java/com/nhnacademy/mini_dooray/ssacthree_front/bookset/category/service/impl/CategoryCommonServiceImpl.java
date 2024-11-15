package com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.adapter.CategoryAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryCommonService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryCommonServiceImpl implements CategoryCommonService {

    private final CategoryAdapter categoryAdapter;

    public CategoryCommonServiceImpl(CategoryAdapter categoryAdapter) {
        this.categoryAdapter = categoryAdapter;
    }

    @Override
    public ResponseEntity<List<CategoryInfoResponse>> getAllCategories() {
        return categoryAdapter.getAllCategories();
    }

    @Override
    public ResponseEntity<CategoryInfoResponse> getCategoryById(Long categoryId) {
        return categoryAdapter.getCategoryById(categoryId);
    }

    @Override
    public ResponseEntity<List<CategoryInfoResponse>> getChildCategories(Long parentCategoryId) {
        return categoryAdapter.getChildCategories(parentCategoryId);
    }

    @Override
    public List<CategoryInfoResponse> getRootCategories() {
        return categoryAdapter.getRootCategories().getBody();
    }

    @Override
    public ResponseEntity<List<CategoryInfoResponse>> searchCategoriesByName(String name) {
        return categoryAdapter.searchCategoriesByName(name);
    }

    @Override
    public List<CategoryInfoResponse> getCategoryPath(Long categoryId) {
        return categoryAdapter.getCategoryPath(categoryId).getBody();
    }

    @Override
    public ResponseEntity<List<CategoryInfoResponse>> getCategoryWithChildren(Long categoryId, int depth) {
        return categoryAdapter.getCategoryWithChildren(categoryId, depth);
    }

    @Override
    public ResponseEntity<List<CategoryInfoResponse>> getAllDescendants(Long categoryId) {
        return categoryAdapter.getAllDescendants(categoryId);
    }

    @Override
    public List<CategoryInfoResponse> flattenCategories(List<CategoryInfoResponse> categories) {
        List<CategoryInfoResponse> flatList = new ArrayList<>();
        for (CategoryInfoResponse category : categories) {
            flattenCategory(category, flatList, 0);
        }
        return flatList;
    }

    private void flattenCategory(CategoryInfoResponse category, List<CategoryInfoResponse> flatList, int depth) {
        // 들여쓰기를 위한 이름 수정 예시
        category.setCategoryName(" ".repeat(depth * 2) + category.getCategoryName());
        flatList.add(category);
        if (category.getChildren() != null) {
            for (CategoryInfoResponse child : category.getChildren()) {
                flattenCategory(child, flatList, depth + 1);
            }
        }
    }

}
