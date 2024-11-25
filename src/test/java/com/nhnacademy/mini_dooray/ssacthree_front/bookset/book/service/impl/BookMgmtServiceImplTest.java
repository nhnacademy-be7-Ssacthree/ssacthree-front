package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter.BookMgmtAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.exception.BookFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.image.adapter.ImageUploadAdapter;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

class BookMgmtServiceImplTest {

    @Mock
    private BookMgmtAdapter bookMgmtAdapter;

    @Mock
    private ImageUploadAdapter imageUploadAdapter;

    @InjectMocks
    private BookMgmtServiceImpl bookMgmtService;

    private static final String[] SORT = {"bookName:asc"};


    private static final String IMAGE_PATH = "/ssacthree/bookImage/";
    private static final String BOOK_SEARCH_ERROR = "책 정보 조회에 실패했습니다.";
    private static final String BOOK_CREATE_ERROR = "책 정보 생성에 실패했습니다.";
    private static final String BOOK_UPDATE_ERROR = "책 정보 수정에 실패했습니다.";


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBooks_ShouldReturnPage_WhenResponseIsSuccessful() {
        // Given
        int page = 0, size = 10;
        List<BookSearchResponse> books = List.of(
            new BookSearchResponse(1L, "Book 1", "Info 1", "Available", null),
            new BookSearchResponse(2L, "Book 2", "Info 2", "Unavailable", null)
        );

        Page<BookSearchResponse> expectedPage = new PageImpl<>(books);
        ResponseEntity<Page<BookSearchResponse>> responseEntity = ResponseEntity.ok(expectedPage);

        Mockito.when(bookMgmtAdapter.getAllBooks(page, size, SORT)).thenReturn(responseEntity);

        // When
        Page<BookSearchResponse> result = bookMgmtService.getAllBooks(page, size, SORT);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Book 1", result.getContent().get(0).getBookName());
        Mockito.verify(bookMgmtAdapter).getAllBooks(page, size, SORT);
    }

    @Test
    void getAllBooks_ShouldThrowBookFailedException_WhenResponseIsUnsuccessful() {
        // Given
        int page = 0, size = 10;
        ResponseEntity<Page<BookSearchResponse>> responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        Mockito.when(bookMgmtAdapter.getAllBooks(page, size, SORT)).thenReturn(responseEntity);

        // When & Then
        BookFailedException exception = assertThrows(BookFailedException.class,
            () -> bookMgmtService.getAllBooks(page, size, SORT));
        assertEquals(BOOK_SEARCH_ERROR, exception.getMessage());
        Mockito.verify(bookMgmtAdapter).getAllBooks(page, size, SORT);
    }

    @Test
    void getAllBooks_ShouldThrowBookFailedException_WhenHttpClientErrorOccurs() {
        // Given
        int page = 0, size = 10;

        Mockito.when(bookMgmtAdapter.getAllBooks(page, size, SORT))
            .thenThrow(new BookFailedException(BOOK_SEARCH_ERROR));

        // When & Then
        BookFailedException exception = assertThrows(BookFailedException.class,
            () -> bookMgmtService.getAllBooks(page, size, SORT));
        assertEquals(BOOK_SEARCH_ERROR, exception.getMessage());
        Mockito.verify(bookMgmtAdapter).getAllBooks(page, size, SORT);
    }

    @Test
    void getAllBooks_ShouldThrowBookFailedException_WhenHttpServerErrorOccurs() {
        // Given
        int page = 0, size = 10;

        Mockito.when(bookMgmtAdapter.getAllBooks(page, size, SORT))
            .thenThrow(new BookFailedException(BOOK_SEARCH_ERROR));

        // When & Then
        BookFailedException exception = assertThrows(BookFailedException.class,
            () -> bookMgmtService.getAllBooks(page, size, SORT));
        assertEquals(BOOK_SEARCH_ERROR, exception.getMessage());
        Mockito.verify(bookMgmtAdapter).getAllBooks(page, size, SORT);
    }

    @Test
    void createBook_ShouldReturnNull_WhenSuccessful() {
        // Given
        MultipartFile thumbnailFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[]{1, 2, 3});
        String expectedImageUrl = "http://example.com/images/image.jpg";

        BookSaveRequestMultipart bookSaveRequestMultipart = new BookSaveRequestMultipart();
        bookSaveRequestMultipart.setBookName("Sample Book");
        bookSaveRequestMultipart.setBookIsbn("1234567890");

        BookSaveRequest expectedBookSaveRequest = new BookSaveRequest();
        expectedBookSaveRequest.setBookName("Sample Book");
        expectedBookSaveRequest.setBookIsbn("1234567890");
        expectedBookSaveRequest.setBookThumbnailImageUrl(expectedImageUrl);

        // Mock image upload success
        Mockito.when(imageUploadAdapter.uploadImage(thumbnailFile, IMAGE_PATH)).thenReturn(expectedImageUrl);

        // Mock book creation success
        ResponseEntity<MessageResponse> mockResponse = new ResponseEntity<>(null, HttpStatus.CREATED);
        Mockito.when(bookMgmtAdapter.createBook(Mockito.any(BookSaveRequest.class))).thenReturn(mockResponse);

        // When
        MessageResponse actualResponse = bookMgmtService.createBook(bookSaveRequestMultipart, thumbnailFile);

        // Then
        assertNull(actualResponse); // 성공 시 응답 메시지가 없는 경우

        Mockito.verify(imageUploadAdapter).uploadImage(thumbnailFile, IMAGE_PATH);
        Mockito.verify(bookMgmtAdapter).createBook(Mockito.any(BookSaveRequest.class));
    }


    @Test
    void createBook_ShouldThrowException_WhenImageUploadFails() {
        // Given
        MultipartFile thumbnailFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[]{1, 2, 3});
        BookSaveRequestMultipart bookSaveRequestMultipart = new BookSaveRequestMultipart();

        Mockito.when(imageUploadAdapter.uploadImage(thumbnailFile, IMAGE_PATH))
            .thenThrow(new RuntimeException("Image upload failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> bookMgmtService.createBook(bookSaveRequestMultipart, thumbnailFile));
        assertEquals("Image upload failed", exception.getMessage());

        Mockito.verify(imageUploadAdapter).uploadImage(thumbnailFile, IMAGE_PATH);
        Mockito.verifyNoInteractions(bookMgmtAdapter); // 도서 생성 호출되지 않아야 함
    }

    @Test
    void createBook_ShouldThrowException_WhenBookCreationFails() {
        // Given
        MultipartFile thumbnailFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[]{1, 2, 3});
        String expectedImageUrl = "http://example.com/images/image.jpg";

        BookSaveRequestMultipart bookSaveRequestMultipart = new BookSaveRequestMultipart();

        // Mock to return expected image URL
        Mockito.when(imageUploadAdapter.uploadImage(thumbnailFile, IMAGE_PATH)).thenReturn(expectedImageUrl);

        // Mock to throw exception on book creation
        Mockito.when(bookMgmtAdapter.createBook(Mockito.any(BookSaveRequest.class)))
            .thenThrow(new BookFailedException(BOOK_CREATE_ERROR));

        // When & Then
        BookFailedException exception = assertThrows(BookFailedException.class,
            () -> bookMgmtService.createBook(bookSaveRequestMultipart, thumbnailFile));

        assertTrue(exception.getMessage().contains(BOOK_CREATE_ERROR));

        // Verify interactions
        Mockito.verify(imageUploadAdapter).uploadImage(thumbnailFile, IMAGE_PATH);
        Mockito.verify(bookMgmtAdapter).createBook(Mockito.argThat(request ->
            expectedImageUrl.equals(request.getBookThumbnailImageUrl())
        ));
    }

//    @Test
//    void updateBook_ShouldThrowException_WhenUpdateFails() {
//        // Given
//        MultipartFile bookThumbnailFile = new MockMultipartFile(
//            "bookThumbnailImageUrlMultipartFile",
//            "NHN Cloud_Logo.png",
//            "image/jpeg",
//            new byte[]{1, 2, 3}
//        );
//
//        BookUpdateRequestMultipart bookUpdateRequestMultipart = new BookUpdateRequestMultipart();
//        String expectedImageUrl = "http://image.toast.com/aaaacko/ssacthree/bookImage/NHN Cloud_Logo.png";
//
//        Mockito.when(bookMgmtService.getImageUrl(bookThumbnailFile)).thenReturn(expectedImageUrl);
//        Mockito.when(bookMgmtService.convertToBookUpdateRequest(bookUpdateRequestMultipart, expectedImageUrl))
//            .thenThrow(new RuntimeException(BOOK_UPDATE_ERROR));
//
//        // When & Then
//        RuntimeException exception = assertThrows(RuntimeException.class,
//            () -> bookMgmtService.updateBook(bookUpdateRequestMultipart, bookThumbnailFile));
//        assertEquals(BOOK_UPDATE_ERROR, exception.getMessage());
//
//        // Verify
//        Mockito.verify(bookMgmtService).getImageUrl(bookThumbnailFile);
//        Mockito.verify(bookMgmtService).convertToBookUpdateRequest(bookUpdateRequestMultipart, expectedImageUrl);
//        Mockito.verifyNoInteractions(bookMgmtAdapter); // updateBook 호출되지 않음을 확인
//    }
//
//
//    @Test
//    void updateBook_ShouldThrowException_WhenUpdateFailsWith4xxOr5xx() {
//        // Given
//        MultipartFile bookThumbnailFile = new MockMultipartFile("file", "image.jpg", "image/jpeg", new byte[]{1, 2, 3});
//        String expectedImageUrl = "http://example.com/images/image.jpg";
//
//        BookUpdateRequestMultipart bookUpdateRequestMultipart = new BookUpdateRequestMultipart();
//        BookUpdateRequest expectedBookUpdateRequest = new BookUpdateRequest();
//
//        Mockito.when(bookMgmtService.getImageUrl(bookThumbnailFile)).thenReturn(expectedImageUrl);
//        Mockito.when(bookMgmtService.convertToBookUpdateRequest(bookUpdateRequestMultipart, expectedImageUrl))
//            .thenReturn(expectedBookUpdateRequest);
//        Mockito.when(bookMgmtAdapter.updateBook(expectedBookUpdateRequest))
//            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
//
//        // When & Then
//        BookFailedException exception = assertThrows(BookFailedException.class,
//            () -> bookMgmtService.updateBook(bookUpdateRequestMultipart, bookThumbnailFile));
//        assertTrue(exception.getMessage().contains(BOOK_UPDATE_ERROR));
//
//        // Verify interactions
//        Mockito.verify(bookMgmtService).getImageUrl(bookThumbnailFile);
//        Mockito.verify(bookMgmtService).convertToBookUpdateRequest(bookUpdateRequestMultipart, expectedImageUrl);
//        Mockito.verify(bookMgmtAdapter).updateBook(expectedBookUpdateRequest);
//    }


}
