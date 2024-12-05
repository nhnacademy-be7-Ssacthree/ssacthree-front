package com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategorySaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request.CategoryUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryAdminService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryCommonService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryAdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class CategoryAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryCommonService categoryCommonService;

    @MockBean
    private CategoryAdminService categoryAdminService;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryInfoResponse categoryResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryResponse = new CategoryInfoResponse(1L, "Category1", true, Collections.emptyList());
    }

    @Test
    void testViewAdminCategories() throws Exception {
        List<CategoryInfoResponse> categories = Arrays.asList(categoryResponse);
        when(categoryCommonService.getAllCategories()).thenReturn(ResponseEntity.ok(categories));

        mockMvc.perform(get("/admin/categories"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("categories"))
            .andExpect(view().name("admin/category/categoryManagement"));

        verify(categoryCommonService).getAllCategories();
    }

    @Test
    void testCreateCategory() throws Exception {

        mockMvc.perform(post("/admin/categories/create")
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/categories"));

        verify(categoryAdminService).createCategory(any(CategorySaveRequest.class));
    }

    @Test
    void testUpdateCategory() throws Exception {
        CategoryUpdateRequest updateRequest = new CategoryUpdateRequest();
        updateRequest.setCategoryName("UpdatedCategory");

        mockMvc.perform(post("/admin/categories/update/1")
                .param("categoryName", updateRequest.getCategoryName()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/categories"));

        verify(categoryAdminService).updateCategory(eq(1L), any(CategoryUpdateRequest.class));
    }

    @Test
    void testDeleteCategory() throws Exception {
        mockMvc.perform(post("/admin/categories/delete/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/categories"));

        verify(categoryAdminService).deleteCategory(1L);
    }


    @Test
    void testCategoryForm() throws Exception {
        // Mock 데이터
        List<CategoryInfoResponse> categories = Arrays.asList(
            new CategoryInfoResponse(1L, "Category1", true, Collections.emptyList()),
            new CategoryInfoResponse(2L, "Category2", true, Collections.emptyList())
        );

        List<CategoryInfoResponse> flatCategories = Arrays.asList(
            new CategoryInfoResponse(1L, "Category1", true, Collections.emptyList()),
            new CategoryInfoResponse(2L, "Category2", true, Collections.emptyList())
        );

        // Mock 서비스 호출 결과
        when(categoryCommonService.getAllCategories()).thenReturn(ResponseEntity.ok(categories));
        when(categoryCommonService.flattenCategories(categories)).thenReturn(flatCategories);

        // Perform 요청 및 결과 검증
        mockMvc.perform(get("/admin/categories/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/category/categoryAddOrEdit"))
            .andExpect(model().attributeExists("categories"))
            .andExpect(model().attribute("categories", flatCategories));

        // 서비스 호출 검증
        verify(categoryCommonService).getAllCategories();
        verify(categoryCommonService).flattenCategories(categories);
    }

    @Test
    void testUpdateCategoryForm() throws Exception {
        // Mock 데이터
        CategoryInfoResponse category = new CategoryInfoResponse(1L, "Category1", true,
            Collections.emptyList());

        // Mock 서비스 호출 결과
        when(categoryCommonService.getCategoryById(1L)).thenReturn(ResponseEntity.ok(category));

        // Perform 요청 및 결과 검증
        mockMvc.perform(get("/admin/categories/update/1"))
            .andExpect(status().isOk()) // 상태 코드 검증
            .andExpect(view().name("admin/category/categoryAddOrEdit")) // 반환된 뷰 이름 검증
            .andExpect(model().attributeExists("category")) // 모델 속성 존재 여부 확인
            .andExpect(model().attribute("category", category)); // 모델 속성 값 검증

        // 서비스 호출 검증
        verify(categoryCommonService).getCategoryById(1L);
    }


}
