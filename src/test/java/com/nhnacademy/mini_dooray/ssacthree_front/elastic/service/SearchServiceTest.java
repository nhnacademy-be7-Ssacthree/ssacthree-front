package com.nhnacademy.mini_dooray.ssacthree_front.elastic.service;

import com.nhnacademy.mini_dooray.ssacthree_front.elastic.adapter.SearchAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.domain.BookDocument;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto.SearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

  private SearchAdapter searchAdapter;
  private SearchService searchService;

  @BeforeEach
  void setUp() {
    searchAdapter = Mockito.mock(SearchAdapter.class);
    searchService = new SearchService(searchAdapter);
  }

  @Test
  void testSearchBooks() {
    // Mock 데이터
    String keyword = "test";
    int page = 0;
    String sort = "score";
    int pageSize = 20;
    Map<String, String> filters = Map.of("category", "fiction", "tag", "bestseller");

    SearchResponse mockResponse = new SearchResponse(10, List.of(
        new BookDocument(1L, "Test Book 1", "index1", "Description 1", "ISBN1",
            "2024-01-01", 10000, 8000, false, 10,
            "url1", 100, 20, "Publisher1", "Author1", List.of("Tag1"), List.of("Category1")),
        new BookDocument(2L, "Test Book 2", "index2", "Description 2", "ISBN2",
            "2024-01-02", 15000, 12000, true, 5,
            "url2", 50, 10, "Publisher2", "Author2", List.of("Tag2"), List.of("Category2"))
    ));

    // SearchAdapter의 동작 모의
    when(searchAdapter.searchBooks(eq(keyword), eq(page), eq(sort), eq(pageSize), eq("fiction"), eq("bestseller")))
        .thenReturn(mockResponse);

    // 서비스 호출
    SearchResponse result = searchService.searchBooks(keyword, page, sort, pageSize, filters);

    // 검증
    assertThat(result).isNotNull();
    assertThat(result.getTotalHits()).isEqualTo(10);
    assertThat(result.getBooks()).hasSize(2);
    assertThat(result.getBooks().get(0).getBookName()).isEqualTo("Test Book 1");

    // SearchAdapter가 올바르게 호출되었는지 확인
    verify(searchAdapter, times(1)).searchBooks(eq(keyword), eq(page), eq(sort), eq(pageSize), eq("fiction"), eq("bestseller"));
  }

  @Test
  void testSearchBooks_NoFilters() {
    // Mock 데이터
    String keyword = "test";
    int page = 0;
    String sort = "score";
    int pageSize = 20;
    Map<String, String> filters = Map.of();

    SearchResponse mockResponse = new SearchResponse(5, List.of(
        new BookDocument(1L, "Test Book 1", "index1", "Description 1", "ISBN1",
            "2024-01-01", 10000, 8000, false, 10,
            "url1", 100, 20, "Publisher1", "Author1", List.of("Tag1"), List.of("Category1"))
    ));

    // SearchAdapter의 동작 모의
    when(searchAdapter.searchBooks(eq(keyword), eq(page), eq(sort), eq(pageSize), isNull(), isNull()))
        .thenReturn(mockResponse);

    // 서비스 호출
    SearchResponse result = searchService.searchBooks(keyword, page, sort, pageSize, filters);

    // 검증
    assertThat(result).isNotNull();
    assertThat(result.getTotalHits()).isEqualTo(5);
    assertThat(result.getBooks()).hasSize(1);

    // SearchAdapter가 올바르게 호출되었는지 확인
    verify(searchAdapter, times(1)).searchBooks(eq(keyword), eq(page), eq(sort), eq(pageSize), isNull(), isNull());
  }
}
