package com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.adapter.CategoryAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class CategoryCommonServiceImplTest {

    @Mock
    private CategoryAdapter categoryAdapter;

    @InjectMocks
    private CategoryCommonServiceImpl categoryCommonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCategories() {
        // Given
        CategoryInfoResponse category = new CategoryInfoResponse(1L, "Category1", true,
            Collections.emptyList());
        when(categoryAdapter.getAllCategories()).thenReturn(ResponseEntity.ok(List.of(category)));

        // When
        ResponseEntity<List<CategoryInfoResponse>> result = categoryCommonService.getAllCategories();

        // Then
        assertThat(result.getBody()).isNotNull();
        verify(categoryAdapter, times(1)).getAllCategories();
    }

    @Test
    void getCategoryById() {
        // Given
        Long categoryId = 1L;
        CategoryInfoResponse category = new CategoryInfoResponse(categoryId, "Category1", true,
            Collections.emptyList());
        when(categoryAdapter.getCategoryById(categoryId)).thenReturn(ResponseEntity.ok(category));

        // When
        ResponseEntity<CategoryInfoResponse> result = categoryCommonService.getCategoryById(
            categoryId);

        // Then
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getCategoryName()).isEqualTo("Category1");
        verify(categoryAdapter, times(1)).getCategoryById(categoryId);
    }

    @Test
    void getChildCategories() {
        // Given
        Long parentCategoryId = 1L;
        CategoryInfoResponse childCategory = new CategoryInfoResponse(2L, "ChildCategory", true,
            Collections.emptyList());
        when(categoryAdapter.getChildCategories(parentCategoryId)).thenReturn(
            ResponseEntity.ok(List.of(childCategory)));

        // When
        ResponseEntity<List<CategoryInfoResponse>> result = categoryCommonService.getChildCategories(
            parentCategoryId);

        // Then
        assertThat(result.getBody()).isNotNull();

        verify(categoryAdapter, times(1)).getChildCategories(parentCategoryId);
    }

    @Test
    void getRootCategories() {
        // Given
        CategoryInfoResponse rootCategory = new CategoryInfoResponse(1L, "RootCategory", true,
            Collections.emptyList());
        when(categoryAdapter.getRootCategories()).thenReturn(
            ResponseEntity.ok(List.of(rootCategory)));

        // When
        List<CategoryInfoResponse> result = categoryCommonService.getRootCategories();

        // Then
        assertThat(result).isNotNull();

        assertThat(result.get(0).getCategoryName()).isEqualTo("RootCategory");
        verify(categoryAdapter, times(1)).getRootCategories();
    }

    @Test
    void flattenCategories() {
        // Given
        CategoryInfoResponse child = new CategoryInfoResponse(2L, "Child", true,
            Collections.emptyList());
        CategoryInfoResponse parent = new CategoryInfoResponse(1L, "Parent", true, List.of(child));
        List<CategoryInfoResponse> categories = List.of(parent);

        // When
        List<CategoryInfoResponse> result = categoryCommonService.flattenCategories(categories);

        // Then
        assertThat(result).isNotNull();

        assertThat(result.get(0).getCategoryName()).isEqualTo("Parent");
        assertThat(result.get(1).getCategoryName().trim()).isEqualTo("Child"); // 들여쓰기 제거 후 비교
    }

    @Test
    void getCategoryWithChildren() {
        // Given
        Long categoryId = 1L;
        int depth = 2;
        CategoryInfoResponse category = new CategoryInfoResponse(categoryId, "Category", true,
            Collections.emptyList());
        when(categoryAdapter.getCategoryWithChildren(categoryId, depth)).thenReturn(
            ResponseEntity.ok(List.of(category)));

        // When
        ResponseEntity<List<CategoryInfoResponse>> result = categoryCommonService.getCategoryWithChildren(
            categoryId, depth);

        // Then
        assertThat(result.getBody()).isNotNull();

        verify(categoryAdapter, times(1)).getCategoryWithChildren(categoryId, depth);
    }

    @Test
    void getCategoryPath() {
        // Given
        Long categoryId = 1L;
        CategoryInfoResponse category = new CategoryInfoResponse(categoryId, "Category", true,
            Collections.emptyList());
        when(categoryAdapter.getCategoryPath(categoryId)).thenReturn(
            ResponseEntity.ok(List.of(category)));

        // When
        List<CategoryInfoResponse> result = categoryCommonService.getCategoryPath(categoryId);

        // Then
        assertThat(result).isNotNull();

        assertThat(result.get(0).getCategoryName()).isEqualTo("Category");
        verify(categoryAdapter, times(1)).getCategoryPath(categoryId);
    }

    @Test
    void searchCategoriesByName() {
        // Given
        String name = "Category";
        CategoryInfoResponse category = new CategoryInfoResponse(1L, "Category", true,
            Collections.emptyList());
        when(categoryAdapter.searchCategoriesByName(name)).thenReturn(
            ResponseEntity.ok(List.of(category)));

        // When
        ResponseEntity<List<CategoryInfoResponse>> result = categoryCommonService.searchCategoriesByName(
            name);

        // Then
        assertThat(result.getBody()).isNotNull();
        
        assertThat(result.getBody().get(0).getCategoryName()).isEqualTo("Category");
        verify(categoryAdapter, times(1)).searchCategoriesByName(name);
    }

    @Test
    void getAllDescendants() {
        // Given
        Long categoryId = 1L;
        CategoryInfoResponse descendant = new CategoryInfoResponse(2L, "DescendantCategory", true,
            Collections.emptyList());
        when(categoryAdapter.getAllDescendants(categoryId)).thenReturn(
            ResponseEntity.ok(List.of(descendant)));

        // When
        ResponseEntity<List<CategoryInfoResponse>> result = categoryCommonService.getAllDescendants(
            categoryId);

        // Then
        assertThat(result.getBody()).isNotNull();

        assertThat(result.getBody().get(0).getCategoryName()).isEqualTo("DescendantCategory");
        verify(categoryAdapter, times(1)).getAllDescendants(categoryId);
    }

}
