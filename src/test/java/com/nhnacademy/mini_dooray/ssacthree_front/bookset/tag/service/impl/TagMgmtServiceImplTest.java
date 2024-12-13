package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.adapter.TagMgmtAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.exception.TagFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

class TagMgmtServiceImplTest {

    @InjectMocks
    private TagMgmtServiceImpl tagMgmtService;

    @Mock
    private TagMgmtAdapter tagMgmtAdapter;

    private static final String[] SORT = {"tagName:asc"};
    private static final String TAG_NOT_FOUND_MESSAGE = "태그 아이디 조회에 실패했습니다.";
    private static final String TAG_SEARCH_ERROR_MESSAGE = "태그 조회에 실패했습니다.";
    private static final String TAG_CREATE_ERROR_MESSAGE = "태그 생성에 실패했습니다.";
    private static final String TAG_UPDATE_ERROR_MESSAGE = "태그 수정에 실패했습니다.";
    private static final String TAG_DELETE_ERROR_MESSAGE = "태그 삭제에 실패했습니다.";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllTags_Success() {
        // Arrange
        Page<TagInfoResponse> mockPage = Mockito.mock(Page.class); // Mock 페이지 객체 생성
        ResponseEntity<Page<TagInfoResponse>> mockResponse = ResponseEntity.ok(mockPage); // 성공적인 ResponseEntity 생성

        Mockito.when(tagMgmtAdapter.getAllTags(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(String[].class)))
            .thenReturn(mockResponse); // Mock 설정

        // Act
        Page<TagInfoResponse> result = tagMgmtService.getAllTags(1, 10, SORT);

        // Assert
        Assertions.assertNotNull(result, "결과가 null이 아님");
        Assertions.assertEquals(mockPage, result, "결과가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1))
            .getAllTags(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(String[].class));
    }

    @Test
    void testGetAllTags_FailedWith4xx() {
        // Arrange
        Mockito.when(tagMgmtAdapter.getAllTags(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(String[].class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)); // 4xx 예외 던지기

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.getAllTags(1, 10, SORT);
        });

        Assertions.assertEquals(TAG_SEARCH_ERROR_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1))
            .getAllTags(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(String[].class));
    }

    @Test
    void testGetAllTags_FailedWith5xx() {
        // Arrange
        Mockito.when(tagMgmtAdapter.getAllTags(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(String[].class)))
            .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)); // 5xx 예외 던지기

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.getAllTags(1, 10, SORT);
        });

        Assertions.assertEquals(TAG_SEARCH_ERROR_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1))
            .getAllTags(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(String[].class));
    }

    @Test
    void testGetAllTags_FailedWithNon2xxResponse() {
        // Arrange
        ResponseEntity<Page<TagInfoResponse>> mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 상태
        Mockito.when(tagMgmtAdapter.getAllTags(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(String[].class)))
            .thenReturn(mockResponse); // Mock 설정

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.getAllTags(1, 10, SORT);
        });

        Assertions.assertEquals(TAG_SEARCH_ERROR_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1))
            .getAllTags(Mockito.anyInt(), Mockito.anyInt(), Mockito.any(String[].class));
    }

    @Test
    void testGetAllTagList_Success() {
        // Arrange
        List<TagInfoResponse> mockTagList = List.of(
            new TagInfoResponse(1L, "Tag1"),
            new TagInfoResponse(2L, "Tag2")
        );
        ResponseEntity<List<TagInfoResponse>> mockResponse = ResponseEntity.ok(mockTagList); // 성공적인 ResponseEntity 생성

        Mockito.when(tagMgmtAdapter.getAllTagList())
            .thenReturn(mockResponse); // Mock 설정

        // Act
        List<TagInfoResponse> result = tagMgmtService.getAllTagList();

        // Assert
        Assertions.assertNotNull(result, "결과가 null이 아님");
        Assertions.assertEquals(2, result.size(), "태그 리스트 크기가 예상과 동일");
        Assertions.assertEquals("Tag1", result.get(0).getTagName(), "첫 번째 태그 이름이 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).getAllTagList();
    }

    @Test
    void testGetAllTagList_FailedWith4xx() {
        // Arrange
        Mockito.when(tagMgmtAdapter.getAllTagList())
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND)); // 4xx 예외 던지기

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.getAllTagList();
        });

        Assertions.assertEquals(TAG_NOT_FOUND_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).getAllTagList();
    }

    @Test
    void testGetAllTagList_FailedWith5xx() {
        // Arrange
        Mockito.when(tagMgmtAdapter.getAllTagList())
            .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)); // 5xx 예외 던지기

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.getAllTagList();
        });

        Assertions.assertEquals(TAG_NOT_FOUND_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).getAllTagList();
    }

    @Test
    void testGetAllTagList_FailedWithNon2xxResponse() {
        // Arrange
        ResponseEntity<List<TagInfoResponse>> mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 상태
        Mockito.when(tagMgmtAdapter.getAllTagList())
            .thenReturn(mockResponse); // Mock 설정

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.getAllTagList();
        });

        Assertions.assertEquals(TAG_NOT_FOUND_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).getAllTagList();
    }

    @Test
    void testCreateTag_Success() {
        // Arrange
        TagCreateRequest request = new TagCreateRequest("NewTag"); // 태그 생성 요청
        MessageResponse mockResponseBody = new MessageResponse("Tag created successfully");
        ResponseEntity<MessageResponse> mockResponse = ResponseEntity.ok(mockResponseBody); // 성공적인 ResponseEntity 생성

        Mockito.when(tagMgmtAdapter.createTag(Mockito.any(TagCreateRequest.class)))
            .thenReturn(mockResponse); // Mock 설정

        // Act
        MessageResponse result = tagMgmtService.createTag(request);

        // Assert
        Assertions.assertNotNull(result, "결과가 null이 아님");
        Assertions.assertEquals("Tag created successfully", result.getMessage(), "메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).createTag(Mockito.any(TagCreateRequest.class));
    }

    @Test
    void testCreateTag_FailedWith4xx() {
        // Arrange
        TagCreateRequest request = new TagCreateRequest("NewTag"); // 태그 생성 요청

        Mockito.when(tagMgmtAdapter.createTag(Mockito.any(TagCreateRequest.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)); // 4xx 예외 던지기

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.createTag(request);
        });

        Assertions.assertEquals(TAG_CREATE_ERROR_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).createTag(Mockito.any(TagCreateRequest.class));
    }

    @Test
    void testCreateTag_FailedWith5xx() {
        // Arrange
        TagCreateRequest request = new TagCreateRequest("NewTag"); // 태그 생성 요청

        Mockito.when(tagMgmtAdapter.createTag(Mockito.any(TagCreateRequest.class)))
            .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)); // 5xx 예외 던지기

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.createTag(request);
        });

        Assertions.assertEquals(TAG_CREATE_ERROR_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).createTag(Mockito.any(TagCreateRequest.class));
    }

    @Test
    void testCreateTag_FailedWithNon2xxResponse() {
        // Arrange
        TagCreateRequest request = new TagCreateRequest("NewTag"); // 태그 생성 요청
        ResponseEntity<MessageResponse> mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 상태

        Mockito.when(tagMgmtAdapter.createTag(Mockito.any(TagCreateRequest.class)))
            .thenReturn(mockResponse); // Mock 설정

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.createTag(request);
        });

        Assertions.assertEquals(TAG_CREATE_ERROR_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).createTag(Mockito.any(TagCreateRequest.class));
    }

    @Test
    void testUpdateTag_Success() {
        // Arrange
        TagUpdateRequest request = new TagUpdateRequest(1L, "UpdatedTag"); // 태그 업데이트 요청
        MessageResponse mockResponseBody = new MessageResponse("Tag updated successfully");
        ResponseEntity<MessageResponse> mockResponse = ResponseEntity.ok(mockResponseBody); // 성공적인 ResponseEntity 생성

        Mockito.when(tagMgmtAdapter.updateTag(Mockito.any(TagUpdateRequest.class)))
            .thenReturn(mockResponse); // Mock 설정

        // Act
        MessageResponse result = tagMgmtService.updateTag(request);

        // Assert
        Assertions.assertNotNull(result, "결과가 null이 아님");
        Assertions.assertEquals("Tag updated successfully", result.getMessage(), "메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).updateTag(Mockito.any(TagUpdateRequest.class));
    }

    @Test
    void testUpdateTag_Failed() {
        // Arrange
        TagUpdateRequest request = new TagUpdateRequest(1L, "UpdatedTag"); // 태그 업데이트 요청

        Mockito.when(tagMgmtAdapter.updateTag(Mockito.any(TagUpdateRequest.class)))
            .thenThrow(new TagFailedException(TAG_UPDATE_ERROR_MESSAGE));

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.updateTag(request);
        });

        Assertions.assertEquals(TAG_UPDATE_ERROR_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).updateTag(Mockito.any(TagUpdateRequest.class));
    }


    @Test
    void testUpdateTag_FailedWithNon2xxResponse() {
        // Arrange
        TagUpdateRequest request = new TagUpdateRequest(1L, "UpdatedTag"); // 태그 업데이트 요청
        ResponseEntity<MessageResponse> mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 상태

        Mockito.when(tagMgmtAdapter.updateTag(Mockito.any(TagUpdateRequest.class)))
            .thenReturn(mockResponse); // Mock 설정

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.updateTag(request);
        });

        Assertions.assertEquals(TAG_UPDATE_ERROR_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).updateTag(Mockito.any(TagUpdateRequest.class));
    }

    @Test
    void testDeleteTag_Success() {
        // Arrange
        Long tagId = 1L; // 삭제할 태그 ID
        MessageResponse mockResponseBody = new MessageResponse("Tag deleted successfully");
        ResponseEntity<MessageResponse> mockResponse = ResponseEntity.ok(mockResponseBody); // 성공적인 ResponseEntity 생성

        Mockito.when(tagMgmtAdapter.deleteTag(Mockito.anyLong()))
            .thenReturn(mockResponse); // Mock 설정

        // Act
        MessageResponse result = tagMgmtService.deleteTag(tagId);

        // Assert
        Assertions.assertNotNull(result, "결과가 null이 아님");
        Assertions.assertEquals("Tag deleted successfully", result.getMessage(), "메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).deleteTag(Mockito.anyLong());
    }

    @Test
    void testDeleteTag_Failed() {
        // Arrange
        Long tagId = 1L; // 삭제할 태그 ID

        Mockito.when(tagMgmtAdapter.deleteTag(Mockito.anyLong()))
            .thenThrow(new TagFailedException(TAG_DELETE_ERROR_MESSAGE));

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.deleteTag(tagId);
        });

        Assertions.assertEquals(TAG_DELETE_ERROR_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).deleteTag(Mockito.anyLong());
    }


    @Test
    void testDeleteTag_FailedWithNon2xxResponse() {
        // Arrange
        Long tagId = 1L; // 삭제할 태그 ID
        ResponseEntity<MessageResponse> mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 상태

        Mockito.when(tagMgmtAdapter.deleteTag(Mockito.anyLong()))
            .thenReturn(mockResponse); // Mock 설정

        // Act & Assert
        TagFailedException exception = Assertions.assertThrows(TagFailedException.class, () -> {
            tagMgmtService.deleteTag(tagId);
        });

        Assertions.assertEquals(TAG_DELETE_ERROR_MESSAGE, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(tagMgmtAdapter, Mockito.times(1)).deleteTag(Mockito.anyLong());
    }



}