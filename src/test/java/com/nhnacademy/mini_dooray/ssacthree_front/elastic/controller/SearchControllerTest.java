package com.nhnacademy.mini_dooray.ssacthree_front.elastic.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.domain.BookDocument;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto.SearchResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
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

  private SearchResponse mockSearchResponse;
  private List<CategoryInfoResponse> mockCategories;

  @BeforeEach
  void setUp() {
    // Mock 검색 결과 초기화
    mockSearchResponse = new SearchResponse(10, List.of());
    mockCategories = List.of(new CategoryInfoResponse(1L, "Category1", true, List.of()));

    // Mock 서비스의 반환값 설정
    when(searchService.searchBooks(Mockito.anyString(), Mockito.anyInt(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyMap()))
        .thenReturn(mockSearchResponse);

    when(categoryCommonService.getAllCategories())
        .thenReturn(ResponseEntity.ok(mockCategories));
  }


  @Test
  @WithMockUser(username = "testUser", roles = {"USER"})
  void testSearchBooksWithResults() throws Exception {
    mockSearchResponse.setBooks(List.of(
        new BookDocument(1L, "Test Book", "1234", "Description", "ISBN",
            "2024-01-01", 10000, 8000, false, 10,
            "url", 100, 20, "Publisher", "Author", List.of("Tag1"), List.of("Category1"))
    ));
    mockSearchResponse.setTotalHits(10);

    mockMvc.perform(get("/search/books")
            .param("keyword", "test")
            .param("page", "1")
            .param("sort", "score")
            .param("pageSize", "20"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("books"))
        .andExpect(model().attributeExists("paging"))
        .andExpect(view().name("searchBooks"));
  }

  @Test
  @WithMockUser(username = "testUser", roles = {"USER"})
  void testSearchBooksNoResults() throws Exception {
    // 검색 요청 시 빈 결과
    mockSearchResponse.setBooks(List.of());

    mockMvc.perform(get("/search/books")
            .param("keyword", "unknown")
            .param("page", "1"))
        .andExpect(status().isOk())
        .andExpect(model().attribute("message", containsString("검색 결과가 없습니다.")))
        .andExpect(view().name("searchBooks"));
  }

  @Test
  @WithMockUser(username = "testUser", roles = {"USER"})
  void testSearchBooksInvalidPage() throws Exception {
    mockMvc.perform(get("/search/books")
            .param("keyword", "test")
            .param("page", "-1")
            .param("pageSize", "20"))
        .andExpect(status().isBadRequest())  // HTTP 400 상태 코드
        .andExpect(view().name("error"))    // 뷰 이름 확인
        .andExpect(model().attribute("exception", "올바르지 않은 페이지 접근")); // 모델 속성 확인
  }


  @Test
  @WithMockUser(username = "testUser", roles = {"USER"})
  void testRedirectCategory() throws Exception {
    // 카테고리 리다이렉션 테스트
    when(categoryCommonService.searchCategoriesByName(Mockito.anyString()))
        .thenReturn(ResponseEntity.ok(List.of(new CategoryInfoResponse(1L, "Category1", true, List.of()))));

    mockMvc.perform(get("/search/redirect")
            .param("category", "Category1"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/books?category-id=1"));
  }
}
