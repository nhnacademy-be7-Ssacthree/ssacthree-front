package com.nhnacademy.mini_dooray.ssacthree_front.elastic.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto.SearchResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.service.SearchService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SearchService searchService;

  @MockBean
  private CategoryCommonService categoryCommonService;

  // 검색 테스트
  @Test
  void testSearchBooks_ValidRequest() throws Exception {
    // Mock 데이터
    SearchResponse mockResponse = new SearchResponse(10, List.of());
    when(searchService.searchBooks(
        Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyMap()
    )).thenReturn(mockResponse);

    List<CategoryInfoResponse> mockCategories = List.of(
        createCategoryInfoResponse(1L, "Fiction", true),
        createCategoryInfoResponse(2L, "Science", true)
    );
    when(categoryCommonService.getAllCategories()).thenReturn(ResponseEntity.ok(mockCategories));

    // 요청 테스트
    mockMvc.perform(get("/search/books")
            .param("keyword", "test")
            .param("page", "1")
            .param("sort", "score")
            .param("pageSize", "20"))
        .andExpect(status().isOk())
        .andExpect(view().name("searchBooks"))
        .andExpect(model().attributeExists("books", "keyword", "categories"));
  }

  // 검색 실패 테스트
  @Test
  void testSearchBooks_InvalidRequest() throws Exception {
    // Mock: 검색 요청이 실패하거나 빈 데이터 반환
    when(searchService.searchBooks(
        Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyMap()
    )).thenReturn(new SearchResponse(0, List.of())); // 빈 결과

    // 요청 테스트
    mockMvc.perform(get("/search/books")
            .param("keyword", "") // 비어 있는 검색어
            .param("page", "1")
            .param("sort", "score")
            .param("pageSize", "20"))
        .andExpect(status().isOk())
        .andExpect(view().name("searchBooks"))
        .andExpect(model().attribute("message", "검색어를 입력해 주세요.")) // 오류 메시지
        .andExpect(model().attribute("books", List.of())); // 빈 데이터 확인
  }


  // 해당 카테고리 책 목록으로 이동
  @Test
  void testRedirectCategory_ValidCategory() throws Exception {
    // Mock 데이터
    List<CategoryInfoResponse> mockCategories = List.of(
        createCategoryInfoResponse(1L, "Fiction", true)
    );
    ResponseEntity<List<CategoryInfoResponse>> responseEntity = ResponseEntity.ok(mockCategories);
    when(categoryCommonService.searchCategoriesByName("Fiction")).thenReturn(responseEntity);

    // 요청 테스트
    mockMvc.perform(get("/search/redirect")
            .param("category", "Fiction"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/books?category-id=1")); // 기대값
  }

  // 없는 카테고리일 때
  @Test
  void testRedirectCategory_NoCategory() throws Exception {
    // Mock 데이터: 빈 리스트 반환
    when(categoryCommonService.searchCategoriesByName("NonExisting"))
        .thenReturn(ResponseEntity.ok(List.of()));

    // 요청 테스트
    mockMvc.perform(get("/search/redirect")
            .param("category", "NonExisting"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/searchBooks")); // 기본 리다이렉트
  }

  @Test
  void testRedirectCategory_NullResponse() throws Exception {
    // Mock 데이터: null 반환
    when(categoryCommonService.searchCategoriesByName("Invalid"))
        .thenReturn(ResponseEntity.ok(null));

    // 요청 테스트
    mockMvc.perform(get("/search/redirect")
            .param("category", "Invalid"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/searchBooks"));
  }


  // 헬퍼 메서드
  private CategoryInfoResponse createCategoryInfoResponse(Long id, String name, boolean isUsed) {
    return new CategoryInfoResponse(id, name, isUsed, List.of());
  }

}
