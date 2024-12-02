package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.TagMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import java.util.Arrays;
import java.util.List;
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
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TagMgmtController.class)
@AutoConfigureMockMvc(addFilters = false)
class TagMgmtControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagMgmtService tagMgmtService;

    private static final String REDIRECT_ADDRESS = "/admin/tags";

    @Test
    void testTagView() throws Exception {
        int page = 0;
        int size = 10;
        String[] sort = {"tagId:asc"};

        // Mock 데이터 설정
        Page<TagInfoResponse> tagsPage = new PageImpl<>(List.of(
            new TagInfoResponse(1L, "Tag1"),
            new TagInfoResponse(2L, "Tag2")
        ));

        Mockito.when(tagMgmtService.getAllTags(page, size, sort)).thenReturn(tagsPage);

        mockMvc.perform(get("/admin/tags")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort[0]))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/tag/Tag"))
            .andExpect(model().attributeExists("baseUrl"))
            .andExpect(model().attributeExists("allParams"))
            .andExpect(model().attributeExists("extraParams"))
            .andExpect(model().attribute("tagList", tagsPage));

        Mockito.verify(tagMgmtService).getAllTags(page, size, sort);
    }

    @Test
    void testTagView_WithExtraParams() throws Exception {
        // Arrange
        int page = 3;
        int size = 10;
        String[] sort = {"tagId:asc"};

        // Mock 데이터 설정
        List<TagInfoResponse> tagsContent = Arrays.asList(
            new TagInfoResponse(1L, "Tag1"),
            new TagInfoResponse(2L, "Tag2")
        );

        Page<TagInfoResponse> tagsPage = new PageImpl<>(tagsContent, PageRequest.of(page, size, Sort.by("tagId").ascending()), tagsContent.size());

        // Mock 서비스 호출 설정
        Mockito.when(tagMgmtService.getAllTags(page, size, sort)).thenReturn(tagsPage);

        // Act & Assert
        mockMvc.perform(get("/admin/tags")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort[0]))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("tagList"))
            .andExpect(model().attributeExists("baseUrl"))
            .andExpect(model().attributeExists("allParams"))
            .andExpect(model().attributeExists("extraParams"))
            .andExpect(model().attribute("tagList", tagsPage))
            .andExpect(model().attribute("baseUrl", "/admin/tags"))
            .andExpect(model().attribute("extraParams", "")) // extraParams가 빈 문자열로 설정된 경우 반영
            .andExpect(view().name("admin/tag/Tag"));

        // 서비스 호출 확인
        Mockito.verify(tagMgmtService).getAllTags(page, size, sort);
    }

    @Test
    void testTagCreate() throws Exception {
        // Arrange
        TagCreateRequest tagCreateRequest = new TagCreateRequest("NewTag");
        MessageResponse mockResponse = new MessageResponse("Tag created successfully");

        // Mock 서비스 동작 설정
        Mockito.when(tagMgmtService.createTag(Mockito.any(TagCreateRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/admin/tags")
                .flashAttr("tagCreateRequest", tagCreateRequest)) // @ModelAttribute로 전달
            .andExpect(status().is3xxRedirection()) // 리다이렉션 상태 확인
            .andExpect(redirectedUrl(REDIRECT_ADDRESS)); // 리다이렉트 URL 확인

        // 서비스 호출 확인
        Mockito.verify(tagMgmtService, Mockito.times(1)).createTag(Mockito.any(TagCreateRequest.class));
    }

    @Test
    void testTagUpdate_Success() throws Exception {
        // Arrange
        TagUpdateRequest tagUpdateRequest = new TagUpdateRequest(1L, "UpdatedTag");
        MessageResponse mockResponse = new MessageResponse("Tag updated successfully");

        // Mock 서비스 동작 설정
        Mockito.when(tagMgmtService.updateTag(Mockito.any(TagUpdateRequest.class))).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/admin/tags/update")
                .flashAttr("tagUpdateRequest", tagUpdateRequest))
            .andExpect(status().is3xxRedirection()) // 리다이렉션 상태 확인
            .andExpect(redirectedUrl(REDIRECT_ADDRESS)); // 리다이렉트 URL 확인

        // 서비스 호출 확인
        Mockito.verify(tagMgmtService, Mockito.times(1)).updateTag(Mockito.any(TagUpdateRequest.class));
    }

    @Test
    void testTagDelete_Success() throws Exception {
        // Arrange
        Long tagId = 1L;
        MessageResponse mockResponse = new MessageResponse("Tag deleted successfully");

        // Mock 서비스 동작 설정
        Mockito.when(tagMgmtService.deleteTag(tagId)).thenReturn(mockResponse);

        // Act & Assert
        mockMvc.perform(post("/admin/tags/delete/{tag-id}", tagId))
            .andExpect(status().is3xxRedirection()) // 리다이렉션 상태 확인
            .andExpect(redirectedUrl(REDIRECT_ADDRESS)); // 리다이렉트 URL 확인

        // 서비스 호출 검증
        Mockito.verify(tagMgmtService, Mockito.times(1)).deleteTag(tagId);
    }

}