package com.nhnacademy.mini_dooray.ssacthree_front.review.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.mini_dooray.ssacthree_front.image.adapter.ImageUploadAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.review.adapter.ReviewAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.*;
import com.nhnacademy.mini_dooray.ssacthree_front.review.exception.PostReviewFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.review.service.impl.ReviewServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

class ReviewServiceTest {

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Mock
    private ReviewAdapter reviewAdapter;

    @Mock
    private ImageUploadAdapter imageUploadAdapter;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testGetReviewsByBookId() {
        // Given
        long bookId = 1L;
        int page = 0;
        int size = 10;
        String[] sort = {"reviewRate,desc"};

        List<BookReviewResponse> mockContent = List.of(
            new BookReviewResponse(
                "User123", // memberId
                5, // reviewRate
                "Excellent Book", // reviewTitle
                "This is a fantastic read!", // reviewContent
                LocalDateTime.now(), // reviewCreatedAt
                "http://example.com/review-image.jpg" // reviewImage
            )
        );

        Page<BookReviewResponse> mockPage = new org.springframework.data.domain.PageImpl<>(
            mockContent);

        when(reviewAdapter.getReviewsByBookId(eq(page), eq(size), eq(sort), eq(bookId)))
            .thenReturn(new ResponseEntity<>(mockPage, HttpStatus.OK));

        // When
        Page<BookReviewResponse> reviewsPage = reviewService.getReviewsByBookId(page, size, sort,
            bookId);

        // Then
        assertNotNull(reviewsPage);
        assertEquals(1, reviewsPage.getTotalElements());
        BookReviewResponse firstReview = reviewsPage.getContent().get(0);
        assertEquals("User123", firstReview.getMemberId());
        assertEquals(5, firstReview.getReviewRate());
        assertEquals("Excellent Book", firstReview.getReviewTitle());
        assertEquals("This is a fantastic read!", firstReview.getReviewContent());
        assertNotNull(firstReview.getReviewCreatedAt());
        verify(reviewAdapter, times(1)).getReviewsByBookId(eq(page), eq(size), eq(sort),
            eq(bookId));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testPostReviewBook() {
        // Given
        Long bookId = 1L;
        Long orderId = 101L;
        ReviewRequest reviewRequest = new ReviewRequest(5, "title", "Amazing book!", null);
        when(request.getCookies()).thenReturn(
            new Cookie[]{new Cookie("access-token", "mock-token")});
        when(imageUploadAdapter.uploadImage(any(), eq("/ssacthree/review/"))).thenReturn(
            "http://image.url");
        when(reviewAdapter.postReviewBook(anyString(), eq(bookId), eq(orderId), any()))
            .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        // When & Then
        assertDoesNotThrow(
            () -> reviewService.postReviewBook(bookId, orderId, reviewRequest, request));
        verify(reviewAdapter, times(1)).postReviewBook(anyString(), eq(bookId), eq(orderId), any());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testAuthToWriteReview() {
        // Given
        Long bookId = 1L;
        when(request.getCookies()).thenReturn(
            new Cookie[]{new Cookie("access-token", "mock-token")});
        when(reviewAdapter.authToWriteReview(anyString(), eq(bookId)))
            .thenReturn(new ResponseEntity<>(101L, HttpStatus.OK));

        // When
        Long orderId = reviewService.authToWriteReview(bookId, request);

        // Then
        assertEquals(101L, orderId);
        verify(reviewAdapter, times(1)).authToWriteReview(anyString(), eq(bookId));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testGetReviewsByMemberId() {
        // Given
        MemberReviewResponse response = new MemberReviewResponse();
        response.setOrderId(101L);
        response.setBookId(1L);
        response.setBookImageUrl(null);
        response.setBookTitle("Great Book");
        response.setReviewRate(5);
        response.setReviewTitle("Amazing!");
        response.setReviewContent("Content");
        response.setReviewImageUrl(null);

        List<MemberReviewResponse> mockResponse = List.of(response);

        when(request.getCookies()).thenReturn(
            new Cookie[]{new Cookie("access-token", "mock-token")});
        when(reviewAdapter.getReviewsByMemberId(anyString()))
            .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        List<MemberReviewResponse> reviews = reviewService.getReviewsByMemberId(request);

        // Then
        assertEquals(1, reviews.size());
        assertEquals("Great Book", reviews.get(0).getBookTitle());
        verify(reviewAdapter, times(1)).getReviewsByMemberId(anyString());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testGetReview() {
        // Given
        Long orderId = 101L;
        Long bookId = 1L;
        ReviewResponse mockResponse = new ReviewResponse(5, "title", "Great book!", null);
        when(request.getCookies()).thenReturn(
            new Cookie[]{new Cookie("access-token", "mock-token")});
        when(reviewAdapter.getReview(anyString(), eq(orderId), eq(bookId)))
            .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        ReviewResponse review = reviewService.getReview(request, orderId, bookId);

        // Then
        assertEquals("Great book!", review.getReviewContent());
        assertEquals(5, review.getReviewRate());
        verify(reviewAdapter, times(1)).getReview(anyString(), eq(orderId), eq(bookId));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testGetAccessToken() {
        // Given
        Cookie[] cookies = {new Cookie("access-token", "mock-token")};
        when(request.getCookies()).thenReturn(cookies);

        // When
        String accessToken = reviewService.getAccessToken(request);

        // Then
        assertEquals("mock-token", accessToken);
    }

    @Test
    void testPostReviewBook_Fail() {
        // Given
        Long bookId = 1L;
        Long orderId = 123L;
        ReviewRequest reviewRequest = new ReviewRequest(5, "Great Book", "Loved it!", null);
        when(request.getCookies()).thenReturn(
            new Cookie[]{new Cookie("access-token", "dummy-token")});
        when(imageUploadAdapter.uploadImage(any(), anyString())).thenReturn(
            "http://example.com/image.jpg");
        doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST))
            .when(reviewAdapter).postReviewBook(anyString(), eq(bookId), eq(orderId), any());

        // When & Then
        assertThrows(PostReviewFailedException.class, () ->
            reviewService.postReviewBook(bookId, orderId, reviewRequest, request)
        );
    }

    @Test
    void testAuthToWriteReview_Fail() {
        // Given
        Long bookId = 1L;
        when(request.getCookies()).thenReturn(
            new Cookie[]{new Cookie("access-token", "dummy-token")});
        doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR))
            .when(reviewAdapter).authToWriteReview(anyString(), eq(bookId));

        // When & Then
        assertThrows(PostReviewFailedException.class, () ->
            reviewService.authToWriteReview(bookId, request)
        );
    }

    @Test
    void testGetReviewsByMemberId_Fail() {
        // Given
        when(request.getCookies()).thenReturn(
            new Cookie[]{new Cookie("access-token", "dummy-token")});
        doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR))
            .when(reviewAdapter).getReviewsByMemberId(anyString());

        // When & Then
        assertThrows(PostReviewFailedException.class, () ->
            reviewService.getReviewsByMemberId(request)
        );
    }

    @Test
    void testGetReview_Fail() {
        // Given
        Long orderId = 123L;
        Long bookId = 1L;
        when(request.getCookies()).thenReturn(
            new Cookie[]{new Cookie("access-token", "dummy-token")});
        doThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND))
            .when(reviewAdapter).getReview(anyString(), eq(orderId), eq(bookId));

        // When & Then
        assertThrows(PostReviewFailedException.class, () ->
            reviewService.getReview(request, orderId, bookId)
        );
    }

    @Test
    void testGetReviewsByBookId_Fail() {
        // Given
        int page = 0;
        int size = 10;
        String[] sort = {"reviewRate,desc"};
        Long bookId = 1L;

        // Mock: API 호출 실패 응답
        when(reviewAdapter.getReviewsByBookId(page, size, sort, bookId))
            .thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

        // When & Then: RuntimeException 발생 확인
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            reviewService.getReviewsByBookId(page, size, sort, bookId)
        );
        assertEquals("API 호출 중 예외 발생", exception.getMessage());
    }

    @Test
    void testPostReviewBook_Fail_StatusCode() {
        // Given
        Long bookId = 1L;
        Long orderId = 123L;
        ReviewRequest reviewRequest = new ReviewRequest(5, "Great Book", "Loved it!", null);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access-token", "dummy-token")});
        when(imageUploadAdapter.uploadImage(any(), anyString())).thenReturn("http://example.com/image.jpg");

        // Mock: API 호출 실패
        doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST))
            .when(reviewAdapter).postReviewBook(anyString(), eq(bookId), eq(orderId), any());

        // When & Then: PostReviewFailedException 발생 확인
        assertThrows(PostReviewFailedException.class, () ->
            reviewService.postReviewBook(bookId, orderId, reviewRequest, request)
        );
    }

    @Test
    void testAuthToWriteReview_Fail_StatusCode() {
        // Given
        Long bookId = 1L;
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access-token", "dummy-token")});

        // Mock: API 호출 실패 응답
        when(reviewAdapter.authToWriteReview(anyString(), eq(bookId)))
            .thenReturn(new ResponseEntity<>(null, HttpStatus.FORBIDDEN));

        // When & Then: RuntimeException 발생 확인
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            reviewService.authToWriteReview(bookId, request)
        );
        assertEquals("이 오류는 뭐지", exception.getMessage());
    }

    @Test
    void testGetReview_Fail_StatusCode() {
        // Given
        Long orderId = 123L;
        Long bookId = 1L;
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access-token", "dummy-token")});

        // Mock: API 호출 실패 응답
        when(reviewAdapter.getReview(anyString(), eq(orderId), eq(bookId)))
            .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        // When & Then: RuntimeException 발생 확인
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            reviewService.getReview(request, orderId, bookId)
        );
        assertEquals("예상치 못한 상태 코드: 404 NOT_FOUND", exception.getMessage());
    }
}

