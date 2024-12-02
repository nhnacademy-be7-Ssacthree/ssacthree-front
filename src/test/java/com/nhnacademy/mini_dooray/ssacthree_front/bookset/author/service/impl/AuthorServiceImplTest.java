package com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.adapter.AuthorAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.exception.AuthorFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AuthorServiceImplTest {

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Mock
    private AuthorAdapter authorAdapter;

    private static final String AUTHOR_SEARCH_ERROR = "작가 정보 조회에 실패했습니다.";
    private static final String AUTHOR_CREATE_ERROR = "작가 정보 생성에 실패했습니다.";
    private static final String AUTHOR_UPDATE_ERROR = "작가 정보 수정에 실패했습니다.";
    private static final String AUTHOR_DELETE_ERROR = "작가 정보 삭제에 실패했습니다.";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAuthorById_Success() {
        // Arrange
        Long authorId = 1L;
        AuthorUpdateRequest mockAuthor = new AuthorUpdateRequest(1L, "Author Name", "Author Info"); // ID 명시적으로 설정
        ResponseEntity<AuthorUpdateRequest> mockResponse = ResponseEntity.ok(mockAuthor);

        Mockito.when(authorAdapter.getAuthorById(authorId)).thenReturn(mockResponse);

        // Act
        AuthorUpdateRequest result = authorService.getAuthorById(authorId);

        // Assert
        Assertions.assertNotNull(result, "결과가 null이 아님");
        Assertions.assertEquals(authorId, result.getAuthorId(), "Author ID가 예상과 동일");
        Assertions.assertEquals("Author Name", result.getAuthorName(), "Author Name이 예상과 동일");
        Assertions.assertEquals("Author Info", result.getAuthorInfo(), "Author Info가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).getAuthorById(authorId);
    }

    @Test
    void testGetAuthorById_Failed() {
        // Arrange
        Long authorId = 1L;

        Mockito.when(authorAdapter.getAuthorById(authorId))
            .thenThrow(new AuthorFailedException(AUTHOR_SEARCH_ERROR));

        // Act & Assert
        AuthorFailedException exception = Assertions.assertThrows(AuthorFailedException.class, () -> {
            authorService.getAuthorById(authorId);
        });

        Assertions.assertEquals(AUTHOR_SEARCH_ERROR, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).getAuthorById(authorId);
    }


    @Test
    void testGetAuthorById_FailedWithNon2xxResponse() {
        // Arrange
        Long authorId = 1L;
        ResponseEntity<AuthorUpdateRequest> mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Mockito.when(authorAdapter.getAuthorById(authorId)).thenReturn(mockResponse);

        // Act & Assert
        AuthorFailedException exception = Assertions.assertThrows(AuthorFailedException.class, () -> {
            authorService.getAuthorById(authorId);
        });

        Assertions.assertEquals(AUTHOR_SEARCH_ERROR, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).getAuthorById(authorId);
    }

    @Test
    void testGetAllAuthors_Success() {
        // Arrange
        int page = 0;
        int size = 10;
        String[] sort = {"authorName:asc"};

        List<AuthorGetResponse> authors = List.of(
            new AuthorGetResponse(1L, "Author 1", "Author 1 Info"),
            new AuthorGetResponse(2L, "Author 2", "Author 2 Info")
        );
        Page<AuthorGetResponse> mockPage = new PageImpl<>(authors);

        ResponseEntity<Page<AuthorGetResponse>> mockResponse = ResponseEntity.ok(mockPage);
        Mockito.when(authorAdapter.getAllAuthors(page, size, sort)).thenReturn(mockResponse);

        // Act
        Page<AuthorGetResponse> result = authorService.getAllAuthors(page, size, sort);

        // Assert
        Assertions.assertNotNull(result, "결과가 null이 아님");
        Assertions.assertEquals(2, result.getContent().size(), "작가 리스트 크기가 예상과 동일");
        Assertions.assertEquals("Author 1", result.getContent().get(0).getAuthorName(), "첫 번째 작가 이름이 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).getAllAuthors(page, size, sort);
    }

    @Test
    void testGetAllAuthors_Failed() {
        // Arrange
        int page = 0;
        int size = 10;
        String[] sort = {"authorName:asc"};

        Mockito.when(authorAdapter.getAllAuthors(page, size, sort))
            .thenThrow(new AuthorFailedException(AUTHOR_SEARCH_ERROR));

        // Act & Assert
        AuthorFailedException exception = Assertions.assertThrows(AuthorFailedException.class, () -> {
            authorService.getAllAuthors(page, size, sort);
        });

        Assertions.assertEquals(AUTHOR_SEARCH_ERROR, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).getAllAuthors(page, size, sort);
    }

    @Test
    void testGetAllAuthors_FailedWithNon2xxResponse() {
        // Arrange
        int page = 0;
        int size = 10;
        String[] sort = {"authorName:asc"};

        ResponseEntity<Page<AuthorGetResponse>> mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Mockito.when(authorAdapter.getAllAuthors(page, size, sort)).thenReturn(mockResponse);

        // Act & Assert
        AuthorFailedException exception = Assertions.assertThrows(AuthorFailedException.class, () -> {
            authorService.getAllAuthors(page, size, sort);
        });

        Assertions.assertEquals(AUTHOR_SEARCH_ERROR, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).getAllAuthors(page, size, sort);
    }

    @Test
    void testGetAllAuthorList_Success() {
        // Arrange
        List<AuthorGetResponse> authors = List.of(
            new AuthorGetResponse(1L, "Author 1", "Author 1 Info"),
            new AuthorGetResponse(2L, "Author 2", "Author 2 Info")
        );
        ResponseEntity<List<AuthorGetResponse>> mockResponse = ResponseEntity.ok(authors);

        Mockito.when(authorAdapter.getAllAuthorList()).thenReturn(mockResponse);

        // Act
        List<AuthorGetResponse> result = authorService.getAllAuthorList();

        // Assert
        Assertions.assertNotNull(result, "결과가 null이 아님");
        Assertions.assertEquals(2, result.size(), "작가 리스트 크기가 예상과 동일");
        Assertions.assertEquals("Author 1", result.get(0).getAuthorName(), "첫 번째 작가 이름이 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).getAllAuthorList();
    }

    @Test
    void testGetAllAuthorList_Failed() {
        // Arrange
        Mockito.when(authorAdapter.getAllAuthorList())
            .thenThrow(new AuthorFailedException(AUTHOR_SEARCH_ERROR));

        // Act & Assert
        AuthorFailedException exception = Assertions.assertThrows(AuthorFailedException.class, () -> {
            authorService.getAllAuthorList();
        });

        Assertions.assertEquals(AUTHOR_SEARCH_ERROR, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).getAllAuthorList();
    }

    @Test
    void testGetAllAuthorList_FailedWithNon2xxResponse() {
        // Arrange
        ResponseEntity<List<AuthorGetResponse>> mockResponse = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Mockito.when(authorAdapter.getAllAuthorList()).thenReturn(mockResponse);

        // Act & Assert
        AuthorFailedException exception = Assertions.assertThrows(AuthorFailedException.class, () -> {
            authorService.getAllAuthorList();
        });

        Assertions.assertEquals(AUTHOR_SEARCH_ERROR, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).getAllAuthorList();
    }

    @Test
    void testGetAllAuthorList_RuntimeException() {
        // Arrange
        Mockito.when(authorAdapter.getAllAuthorList()).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        RuntimeException exception = Assertions.assertThrows(RuntimeException.class, () -> {
            authorService.getAllAuthorList();
        });

        Assertions.assertEquals("Unexpected error", exception.getMessage(), "RuntimeException 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).getAllAuthorList();
    }


    @Test
    void testCreateAuthor_Success() {
        // Arrange
        AuthorCreateRequest authorCreateRequest = new AuthorCreateRequest("Author Name", "Author Info");
        MessageResponse mockResponse = new MessageResponse("Author created successfully");

        ResponseEntity<MessageResponse> mockResponseEntity = ResponseEntity.ok(mockResponse);
        Mockito.when(authorAdapter.createAuthor(Mockito.any(AuthorCreateRequest.class))).thenReturn(mockResponseEntity);

        // Act
        MessageResponse result = authorService.createAuthor(authorCreateRequest);

        // Assert
        Assertions.assertNotNull(result, "결과가 null이 아님");
        Assertions.assertEquals("Author created successfully", result.getMessage(), "메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).createAuthor(Mockito.any(AuthorCreateRequest.class));
    }

    @Test
    void testCreateAuthor_Failed() {
        // Arrange
        AuthorCreateRequest authorCreateRequest = new AuthorCreateRequest("Author Name", "Author Info");

        Mockito.when(authorAdapter.createAuthor(Mockito.any(AuthorCreateRequest.class)))
            .thenThrow(new AuthorFailedException(AUTHOR_CREATE_ERROR));

        // Act & Assert
        AuthorFailedException exception = Assertions.assertThrows(AuthorFailedException.class, () -> {
            authorService.createAuthor(authorCreateRequest);
        });

        Assertions.assertEquals(AUTHOR_CREATE_ERROR, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).createAuthor(Mockito.any(AuthorCreateRequest.class));
    }

    @Test
    void testCreateAuthor_FailedWithNon2xxResponse() {
        // Arrange
        AuthorCreateRequest authorCreateRequest = new AuthorCreateRequest("Author Name", "Author Info");
        ResponseEntity<MessageResponse> mockResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Mockito.when(authorAdapter.createAuthor(Mockito.any(AuthorCreateRequest.class))).thenReturn(mockResponseEntity);

        // Act & Assert
        AuthorFailedException exception = Assertions.assertThrows(AuthorFailedException.class, () -> {
            authorService.createAuthor(authorCreateRequest);
        });

        Assertions.assertEquals(AUTHOR_CREATE_ERROR, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).createAuthor(Mockito.any(AuthorCreateRequest.class));
    }

    @Test
    void testUpdateAuthor_Success() {
        // Arrange
        AuthorUpdateRequest authorUpdateRequest = new AuthorUpdateRequest(1L, "Updated Author", "Updated Info");
        MessageResponse mockResponse = new MessageResponse("Author updated successfully");

        ResponseEntity<MessageResponse> mockResponseEntity = ResponseEntity.ok(mockResponse);
        Mockito.when(authorAdapter.updateAuthor(Mockito.any(AuthorUpdateRequest.class))).thenReturn(mockResponseEntity);

        // Act
        MessageResponse result = authorService.updateAuthor(authorUpdateRequest);

        // Assert
        Assertions.assertNotNull(result, "결과가 null이 아님");
        Assertions.assertEquals("Author updated successfully", result.getMessage(), "메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).updateAuthor(Mockito.any(AuthorUpdateRequest.class));
    }

    @Test
    void testUpdateAuthor_Failed() {
        // Arrange
        AuthorUpdateRequest authorUpdateRequest = new AuthorUpdateRequest(1L, "Updated Author", "Updated Info");

        Mockito.when(authorAdapter.updateAuthor(Mockito.any(AuthorUpdateRequest.class)))
            .thenThrow(new AuthorFailedException(AUTHOR_UPDATE_ERROR));

        // Act & Assert
        AuthorFailedException exception = Assertions.assertThrows(AuthorFailedException.class, () -> {
            authorService.updateAuthor(authorUpdateRequest);
        });

        Assertions.assertEquals(AUTHOR_UPDATE_ERROR, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).updateAuthor(Mockito.any(AuthorUpdateRequest.class));
    }

    @Test
    void testUpdateAuthor_FailedWithNon2xxResponse() {
        // Arrange
        AuthorUpdateRequest authorUpdateRequest = new AuthorUpdateRequest(1L, "Updated Author", "Updated Info");
        ResponseEntity<MessageResponse> mockResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Mockito.when(authorAdapter.updateAuthor(Mockito.any(AuthorUpdateRequest.class))).thenReturn(mockResponseEntity);

        // Act & Assert
        AuthorFailedException exception = Assertions.assertThrows(AuthorFailedException.class, () -> {
            authorService.updateAuthor(authorUpdateRequest);
        });

        Assertions.assertEquals(AUTHOR_UPDATE_ERROR, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).updateAuthor(Mockito.any(AuthorUpdateRequest.class));
    }

    @Test
    void testDeleteAuthor_Success() {
        // Arrange
        Long authorId = 1L;
        MessageResponse mockResponse = new MessageResponse("Author deleted successfully");

        ResponseEntity<MessageResponse> mockResponseEntity = ResponseEntity.ok(mockResponse);
        Mockito.when(authorAdapter.deleteAuthor(authorId)).thenReturn(mockResponseEntity);

        // Act
        MessageResponse result = authorService.deleteAuthor(authorId);

        // Assert
        Assertions.assertNotNull(result, "결과가 null이 아님");
        Assertions.assertEquals("Author deleted successfully", result.getMessage(), "메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).deleteAuthor(authorId);
    }

    @Test
    void testDeleteAuthor_Failed() {
        // Arrange
        Long authorId = 1L;

        Mockito.when(authorAdapter.deleteAuthor(authorId))
            .thenThrow(new AuthorFailedException(AUTHOR_DELETE_ERROR));

        // Act & Assert
        AuthorFailedException exception = Assertions.assertThrows(AuthorFailedException.class, () -> {
            authorService.deleteAuthor(authorId);
        });

        Assertions.assertEquals(AUTHOR_DELETE_ERROR, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).deleteAuthor(authorId);
    }

    @Test
    void testDeleteAuthor_FailedWithNon2xxResponse() {
        // Arrange
        Long authorId = 1L;
        ResponseEntity<MessageResponse> mockResponseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Mockito.when(authorAdapter.deleteAuthor(authorId)).thenReturn(mockResponseEntity);

        // Act & Assert
        AuthorFailedException exception = Assertions.assertThrows(AuthorFailedException.class, () -> {
            authorService.deleteAuthor(authorId);
        });

        Assertions.assertEquals(AUTHOR_DELETE_ERROR, exception.getMessage(), "예외 메시지가 예상과 동일");
        Mockito.verify(authorAdapter, Mockito.times(1)).deleteAuthor(authorId);
    }

}