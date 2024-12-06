package com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.adapter.CategoryAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

class CategoryAdminServiceImplTest {

    @Mock
    private CategoryAdapter categoryAdapter;

    @InjectMocks
    private CategoryAdminServiceImpl categoryAdminService;

    public CategoryAdminServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCategory() {
        // Given
        CategorySaveRequest request = new CategorySaveRequest("Fiction", null);
        CategoryInfoResponse response = new CategoryInfoResponse(1L, "Fiction", true,
            Collections.emptyList());
        when(categoryAdapter.createCategory(request)).thenReturn(ResponseEntity.ok(response));

        // When
        ResponseEntity<CategoryInfoResponse> result = categoryAdminService.createCategory(request);

        // Then
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getCategoryName()).isEqualTo("Fiction");
        verify(categoryAdapter, times(1)).createCategory(request);
    }

    @Test
    void updateCategory() {
        // Given
        Long categoryId = 1L;
        CategoryUpdateRequest request = new CategoryUpdateRequest("Non-Fiction", null);
        CategoryInfoResponse response = new CategoryInfoResponse(1L, "Non-Fiction", true,
            Collections.emptyList());
        when(categoryAdapter.updateCategory(categoryId, request)).thenReturn(
            ResponseEntity.ok(response));

        // When
        ResponseEntity<CategoryInfoResponse> result = categoryAdminService.updateCategory(
            categoryId, request);

        // Then
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getCategoryName()).isEqualTo("Non-Fiction");
        verify(categoryAdapter, times(1)).updateCategory(categoryId, request);
    }

    @Test
    void deleteCategory() {
        // Given
        Long categoryId = 1L;
        when(categoryAdapter.deleteCategory(categoryId)).thenReturn(ResponseEntity.ok(true));

        // When
        ResponseEntity<Boolean> result = categoryAdminService.deleteCategory(categoryId);

        // Then
        assertThat(result.getBody()).isTrue();
        verify(categoryAdapter, times(1)).deleteCategory(categoryId);
    }

    @Test
    void getAllCategoriesForAdmin() {
        // Given
        CategoryInfoResponse response = new CategoryInfoResponse(1L, "Fiction", true,
            Collections.emptyList());
        when(categoryAdapter.getAllCategoriesForAdmin()).thenReturn(
            ResponseEntity.ok(List.of(response)));

        // When
        ResponseEntity<List<CategoryInfoResponse>> result = categoryAdminService.getAllCategoriesForAdmin();

        // Then
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().get(0).getCategoryName()).isEqualTo("Fiction");
        verify(categoryAdapter, times(1)).getAllCategoriesForAdmin();
    }
}
