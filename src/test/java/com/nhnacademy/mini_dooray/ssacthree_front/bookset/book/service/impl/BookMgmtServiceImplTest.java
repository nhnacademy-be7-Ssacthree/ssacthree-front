package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter.BookMgmtAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.exception.BookFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.image.adapter.ImageUploadAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

class BookMgmtServiceImplTest {

    @Mock
    private BookMgmtAdapter bookMgmtAdapter;

    @Mock
    private ImageUploadAdapter imageUploadAdapter;

    @InjectMocks
    private BookMgmtServiceImpl bookMgmtService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks_success() {
        // Given
        Page<BookSearchResponse> mockPage = Page.empty();
        given(bookMgmtAdapter.getAllBooks(anyInt(), anyInt(), any(String[].class)))
            .willReturn(new ResponseEntity<>(mockPage, HttpStatus.OK));

        // When
        var result = bookMgmtService.getAllBooks(0, 10, new String[]{"name,asc"});

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    void testGetAllBooks_failure() {
        // Given: 실제 서버와의 연결 없이 bookMgmtAdapter를 모의(mock) 처리
        given(bookMgmtAdapter.getAllBooks(anyInt(), anyInt(), any(String[].class)))
            .willThrow(HttpClientErrorException.class); // HttpClientErrorException 발생 시뮬레이션

        // When / Then: 실제 서버 없이 예외를 확인
        assertThatThrownBy(() -> bookMgmtService.getAllBooks(0, 10, new String[]{"name,asc"}))
            .isInstanceOf(BookFailedException.class)
            .hasMessageContaining("책 정보 조회에 실패했습니다.");
    }




    @Test
    void testCreateBook_success() {
        // Given
        BookSaveRequestMultipart requestMultipart = new BookSaveRequestMultipart();
        MultipartFile mockFile = null; // 필요 시 MockMultipartFile 사용 가능
        given(imageUploadAdapter.uploadImage(any(), eq("/ssacthree/bookImage/"))).willReturn("http://example.com/image.jpg");

        BookSaveRequest bookSaveRequest = new BookSaveRequest();
        given(bookMgmtAdapter.createBook(any(BookSaveRequest.class)))
            .willReturn(new ResponseEntity<>(new MessageResponse("Success"), HttpStatus.OK));

        // When
        MessageResponse response = bookMgmtService.createBook(requestMultipart, mockFile);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getMessage()).isEqualTo("Success");
    }

    @Test
    void testCreateBook_failure() {
        // Given
        BookSaveRequestMultipart requestMultipart = new BookSaveRequestMultipart();
        MultipartFile mockFile = null;
        given(imageUploadAdapter.uploadImage(any(), eq("/ssacthree/bookImage/"))).willReturn("http://example.com/image.jpg");

        given(bookMgmtAdapter.createBook(any(BookSaveRequest.class)))
            .willThrow(HttpClientErrorException.class);

        // When / Then
        assertThatThrownBy(() -> bookMgmtService.createBook(requestMultipart, mockFile))
            .isInstanceOf(BookFailedException.class) // Ensure BookFailedException is thrown
            .hasMessageContaining("책 정보 생성에 실패했습니다.");
    }

    @Test
    void testGetImageUrl_success() {
        // Given
        MultipartFile mockFile = null; // 필요 시 MockMultipartFile로 대체 가능
        given(imageUploadAdapter.uploadImage(any(), eq("/ssacthree/bookImage/"))).willReturn("http://example.com/image.jpg");

        // When
        String imageUrl = bookMgmtService.getImageUrl(mockFile);

        // Then
        assertThat(imageUrl).isEqualTo("http://example.com/image.jpg");
    }
}
