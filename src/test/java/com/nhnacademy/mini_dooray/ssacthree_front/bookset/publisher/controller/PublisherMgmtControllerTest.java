package com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.service.PublisherMgmtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PublisherMgmtController.class)
class PublisherMgmtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherMgmtService publisherMgmtService;

    @Test
    @DisplayName("GET /admin/publishers - 성공")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testGetPublishers() throws Exception {
        // Mock 데이터 설정
        Page<PublisherGetResponse> mockPage = new PageImpl<>(List.of(
                new PublisherGetResponse(1L, "Publisher A", true),
                new PublisherGetResponse(2L, "Publisher B", false)
        ));

        // Mock 동작 설정
        given(publisherMgmtService.getAllPublishers(0, 10, new String[]{"bookName:asc"}))
                .willReturn(mockPage);

        // 테스트 수행
        mockMvc.perform(get("/admin/publishers")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("publishers"))
                .andExpect(model().attribute("publishers", mockPage))
                .andExpect(view().name("admin/publisher/publishers"));
    }

    @Test
    @DisplayName("POST /admin/publishers - 성공")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testUpdatePublisher_Success() throws Exception {
        mockMvc.perform(post("/admin/publishers")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("publisherId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/publishers"));

        Mockito.verify(publisherMgmtService).updatePublisher(any(PublisherUpdateRequest.class));
    }

//    @Test
//    @DisplayName("POST /admin/publishers - 유효성 검증 실패")
//    @WithMockUser(username = "testUser", roles = {"ADMIN"})
//    void testUpdatePublisher_ValidationFailed() throws Exception {
//        Mockito.doThrow(new ValidationFailedException(Mockito.mock(BindingResult.class)))
//                .when(publisherMgmtService).updatePublisher(any(PublisherUpdateRequest.class));
//
//        mockMvc.perform(post("/admin/publishers")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    @DisplayName("GET /admin/publishers/create - 성공")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testGetCreatePublisherPage() throws Exception {
        mockMvc.perform(get("/admin/publishers/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/publisher/createPublisher"));
    }

    @Test
    @DisplayName("POST /admin/publishers/create - 성공")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testCreatePublisher_Success() throws Exception {
        mockMvc.perform(post("/admin/publishers/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("publisherName", "Test Publisher"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/publishers"));

        Mockito.verify(publisherMgmtService).createPublisher(any(PublisherCreateRequest.class));
    }

//    @Test
//    @DisplayName("POST /admin/publishers/create - 유효성 검증 실패")
//    @WithMockUser(username = "testUser", roles = {"ADMIN"})
//    void testCreatePublisher_ValidationFailed() throws Exception {
//        Mockito.doThrow(new ValidationFailedException(Mockito.mock(BindingResult.class)))
//                .when(publisherMgmtService).createPublisher(any(PublisherCreateRequest.class));
//
//        mockMvc.perform(post("/admin/publishers/create")
//                        .with(csrf())
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//                .andExpect(status().isBadRequest());
//    }
}
