//package com.nhnacademy.mini_dooray.ssacthree_front.elastic.service;
//
//import com.nhnacademy.mini_dooray.ssacthree_front.elastic.adapter.SearchAdapter;
//import com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto.SearchResponse;
//import java.util.List;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.Map;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//class SearchServiceTest {
//
//  @Mock
//  private SearchAdapter searchAdapter;
//
//  @InjectMocks
//  private SearchService searchService;
//
//  @Test
//  void testSearchBooks() {
//    // Mock 데이터 생성
//    SearchResponse mockResponse = new SearchResponse(10, List.of());
//
//    // Mock 설정
//    when(searchAdapter.searchBooks(
//        Mockito.eq("test"),    // keyword
//        Mockito.eq(0),         // page
//        Mockito.eq("score"),   // sort
//        Mockito.eq(20),        // pageSize
//        Mockito.eq("Fiction"), // category
//        Mockito.isNull()       // tag (null로 설정)
//    )).thenReturn(mockResponse);
//
//    // Act
//    SearchResponse response = searchService.searchBooks(
//        "test", 0, "score", 20, Map.of("category", "Fiction")
//    );
//
//    // Assert
//    assertThat(response).isNotNull();
//    assertThat(response.getTotalHits()).isEqualTo(10);
//    assertThat(response.getBooks()).isEmpty();
//  }
//}
