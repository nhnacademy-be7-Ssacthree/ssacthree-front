package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.request.BookLikeRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.response.BookLikeResponse;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
class MemberLikeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BookCommonService bookCommonService;

    @InjectMocks
    private MemberLikeController memberLikeController;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberLikeController).build();
    }

    @Test
    void testCreateBookLikeByMemberId() throws Exception {
        // BookLikeRequest 객체 생성 및 필드 설정 (리플렉션 사용)
        BookLikeRequest request = new BookLikeRequest();
        setField(request, "bookId", 1L);

        // BookLikeResponse 객체 생성 및 필드 설정 (리플렉션 사용)
        BookLikeResponse response = new BookLikeResponse();
        setField(response, "bookId", 1L);
        setField(response, "likeCount", 10L);

        // 서비스 메서드 모킹
        when(bookCommonService.createBookLikeByMemberId(any(BookLikeRequest.class))).thenReturn(
            response);

        // POST 요청 및 결과 검증
        mockMvc.perform(post("/shop/members/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.bookId", is(1)))
            .andExpect(jsonPath("$.likeCount", is(10)));
    }

    @Test
    void testDeleteBookLikeByMemberId() throws Exception {
        Long bookId = 1L;

        // 서비스 메서드 모킹
        when(bookCommonService.deleteBookLikeByMemberId(bookId)).thenReturn(true);

        // DELETE 요청 및 결과 검증
        mockMvc.perform(delete("/shop/members/likes/{book-id}", bookId))
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
    }

    // 리플렉션을 사용하여 private 필드 값 설정하는 유틸리티 메서드
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
