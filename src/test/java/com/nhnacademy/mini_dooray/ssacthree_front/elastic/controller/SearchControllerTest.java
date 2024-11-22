//package com.nhnacademy.mini_dooray.ssacthree_front.elastic.controller;
//
//import com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto.CategoryInfoResponse;
//import com.nhnacademy.mini_dooray.ssacthree_front.elastic.service.CategoryCommonService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(SearchController.class)
//class SearchControllerTest {
//
//  @Autowired
//  private MockMvc mockMvc;
//
//  @MockBean
//  private CategoryCommonService categoryCommonService;
//
//  private HttpHeaders httpHeaders;
//
//  @BeforeEach
//  void setUp() {
//    httpHeaders = new HttpHeaders();
//    String auth = "Basic " + java.util.Base64.getEncoder().encodeToString("username:password".getBytes());
//    httpHeaders.add(HttpHeaders.AUTHORIZATION, auth);
//  }
//
//  @Test
//  void testRedirectCategory_Found() throws Exception {
//    // Mocking service response
//    CategoryInfoResponse categoryInfo = new CategoryInfoResponse(1L, "categoryName", true, Collections.emptyList());
//    when(categoryCommonService.searchCategoriesByName(anyString()))
//        .thenReturn(new ResponseEntity<>(List.of(categoryInfo), HttpStatus.OK));
//
//    // Perform request
//    mockMvc.perform(get("/redirect")
//            .param("category", "testCategory")
//            .headers(httpHeaders)
//            .contentType(MediaType.APPLICATION_JSON))
//        .andExpect(status().is3xxRedirection())
//        .andExpect(redirectedUrl("/books?category-id=1"));
//  }
//
//  @Test
//  void testRedirectCategory_NotFound() throws Exception {
//    // Mocking service response with empty list
//    when(categoryCommonService.searchCategoriesByName(anyString()))
//        .thenReturn(new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK));
//
//    // Perform request
//    mockMvc.perform(get("/redirect")
//            .param("category", "nonExistentCategory")
//            .headers(httpHeaders)
//            .contentType(MediaType.APPLICATION_JSON))
//        .andExpect(status().is3xxRedirection())
//        .andExpect(redirectedUrl("/searchBooks"));
//  }
//
//  @Test
//  void testRedirectCategory_ServiceError() throws Exception {
//    // Mocking service response with error
//    when(categoryCommonService.searchCategoriesByName(anyString()))
//        .thenReturn(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
//
//    // Perform request
//    mockMvc.perform(get("/redirect")
//            .param("category", "errorCategory")
//            .headers(httpHeaders)
//            .contentType(MediaType.APPLICATION_JSON))
//        .andExpect(status().is3xxRedirection())
//        .andExpect(redirectedUrl("/searchBooks"));
//  }
//}
