package com.nhnacademy.mini_dooray.ssacthree_front.review.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.nhnacademy.mini_dooray.ssacthree_front.image.adapter.ImageUploadAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.review.adapter.ReviewAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.*;
import com.nhnacademy.mini_dooray.ssacthree_front.review.service.impl.ReviewServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.springframework.security.test.context.support.WithMockUser;

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
        Long bookId = 1L;
        List<BookReviewResponse> mockResponse = List.of(
            new BookReviewResponse(
                "User123", // memberId
                5, // reviewRate
                "Excellent Book", // reviewTitle
                "This is a fantastic read!", // reviewContent
                LocalDateTime.now(), // reviewCreatedAt
                "http://example.com/review-image.jpg" // reviewImage
            )
        );

        when(reviewAdapter.getReviewsByBookId(bookId))
            .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // When
        List<BookReviewResponse> reviews = reviewService.getReviewsByBookId(bookId);

        // Then
        assertEquals(1, reviews.size());
        assertEquals("User123", reviews.getFirst().getMemberId());
        assertEquals(5, reviews.getFirst().getReviewRate());
        assertEquals("Excellent Book", reviews.getFirst().getReviewTitle());
        assertEquals("This is a fantastic read!", reviews.getFirst().getReviewContent());
        assertNotNull(reviews.getFirst().getReviewCreatedAt());
        verify(reviewAdapter, times(1)).getReviewsByBookId(bookId);
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testPostReviewBook() {
        // Given
        Long bookId = 1L;
        Long orderId = 101L;
        ReviewRequest reviewRequest = new ReviewRequest(5,"title","Amazing book!", null);
        when(request.getCookies()).thenReturn(new Cookie[] {new Cookie("access-token", "mock-token")});
        when(imageUploadAdapter.uploadImage(any(), eq("/ssacthree/review/"))).thenReturn("http://image.url");
        when(reviewAdapter.postReviewBook(anyString(), eq(bookId), eq(orderId), any()))
            .thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        // When & Then
        assertDoesNotThrow(() -> reviewService.postReviewBook(bookId, orderId, reviewRequest, request));
        verify(reviewAdapter, times(1)).postReviewBook(anyString(), eq(bookId), eq(orderId), any());
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testAuthToWriteReview() {
        // Given
        Long bookId = 1L;
        when(request.getCookies()).thenReturn(new Cookie[] {new Cookie("access-token", "mock-token")});
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

        when(request.getCookies()).thenReturn(new Cookie[] {new Cookie("access-token", "mock-token")});
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
        ReviewResponse mockResponse = new ReviewResponse(5,"title","Great book!", null);
        when(request.getCookies()).thenReturn(new Cookie[] {new Cookie("access-token", "mock-token")});
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
}