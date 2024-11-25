package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.adapter.TagMgmtAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.exception.TagFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TagMgmtServiceImplTest {

    @InjectMocks
    private TagMgmtServiceImpl tagMgmtService;

    @Mock
    private TagMgmtAdapter tagMgmtAdapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTags_success() {
        // Given
        Page<TagInfoResponse> expectedPage = mock(Page.class);
        ResponseEntity<Page<TagInfoResponse>> responseEntity = ResponseEntity.ok(expectedPage);
        when(tagMgmtAdapter.getAllTags(anyInt(), anyInt(), any(String[].class))).thenReturn(responseEntity);

        // When
        Page<TagInfoResponse> actual = tagMgmtService.getAllTags(0, 10, new String[]{"name,asc"});

        // Then
        assertThat(actual).isEqualTo(expectedPage);
    }

//    @Test
//    void testGetAllTags_failure() {
//        // Given
//        when(tagMgmtAdapter.getAllTags(anyInt(), anyInt(), any(String[].class)))
//            .thenThrow(FeignException.class);  // FeignException으로 예외를 던지도록 설정
//
//        // When
//        Page<TagInfoResponse> actual = tagMgmtService.getAllTags(0, 10, new String[]{"name,asc"});
//
//        // Then
//        assertThat(actual).isEqualTo(Page.empty());  // 빈 페이지 반환 확인
//    }

    @Test
    void testGetAllTagList_success() {
        // Given
        List<TagInfoResponse> expectedList = mock(List.class);
        ResponseEntity<List<TagInfoResponse>> responseEntity = ResponseEntity.ok(expectedList);
        when(tagMgmtAdapter.getAllTagList()).thenReturn(responseEntity);

        // When
        List<TagInfoResponse> actual = tagMgmtService.getAllTagList();

        // Then
        assertThat(actual).isEqualTo(expectedList);
    }

    @Test
    void testGetAllTagList_failure() {
        // Given
        when(tagMgmtAdapter.getAllTagList())
            .thenThrow(HttpServerErrorException.class);

        // When / Then
        try {
            tagMgmtService.getAllTagList();
        } catch (TagFailedException e) {
            assertThat(e.getMessage()).isEqualTo("태그 아이디 조회에 실패했습니다.");
        }
    }

    @Test
    void testCreateTag_success() {
        // Given
        TagCreateRequest tagCreateRequest = new TagCreateRequest("NewTag");
        MessageResponse expectedResponse = new MessageResponse("태그가 성공적으로 생성되었습니다.");
        ResponseEntity<MessageResponse> responseEntity = ResponseEntity.ok(expectedResponse);
        when(tagMgmtAdapter.createTag(any(TagCreateRequest.class))).thenReturn(responseEntity);

        // When
        MessageResponse actual = tagMgmtService.createTag(tagCreateRequest);

        // Then
        assertThat(actual).isEqualTo(expectedResponse);
    }

    @Test
    void testCreateTag_failure() {
        // Given
        TagCreateRequest tagCreateRequest = new TagCreateRequest("NewTag");
        when(tagMgmtAdapter.createTag(any(TagCreateRequest.class)))
            .thenThrow(FeignException.class);

        // When / Then
        try {
            tagMgmtService.createTag(tagCreateRequest);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("태그 생성이 불가능합니다.");
        }
    }
}
