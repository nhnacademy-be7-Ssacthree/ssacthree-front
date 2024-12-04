package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookRestController.class)
@AutoConfigureMockMvc(addFilters = false)
class BookRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCommonService bookCommonService;

    private Page<BookListResponse> mockBookPage;

    @BeforeEach
    void setUp() {
        BookListResponse mockBook = new BookListResponse();
        mockBookPage = new PageImpl<>(Collections.singletonList(mockBook),
            PageRequest.of(0, 10, Sort.by("publicationDate").descending()),
            1);
    }

    @Test
    void testGetBooksByCategoryId() throws Exception {
        Mockito.when(bookCommonService.getBooksByCategoryId(anyInt(), anyInt(), any(), anyLong()))
            .thenReturn(mockBookPage);

        mockMvc.perform(get("/api/shop/books/categories/1")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "bookName:asc")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void testGetAllBooks() throws Exception {
        Mockito.when(bookCommonService.getAllAvailableBooks(anyInt(), anyInt(), any()))
            .thenReturn(mockBookPage);

        mockMvc.perform(get("/api/shop/books")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "bookName:asc")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
