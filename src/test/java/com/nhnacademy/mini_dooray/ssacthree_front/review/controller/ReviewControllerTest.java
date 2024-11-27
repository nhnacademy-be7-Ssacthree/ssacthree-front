package com.nhnacademy.mini_dooray.ssacthree_front.review.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.MemberReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testPostReview() throws Exception {
        ReviewRequest review = new ReviewRequest(5,"Amazing read!", "review",null);

        mockMvc.perform(post("/shop/members/reviews")
                .param("bookId", "1")
                .param("orderId", "101")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("reviewRequest", review)
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/members/my-page/reviews"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testAuthToWriteReview() throws Exception {
        Long bookId = 1L;
        Long orderId = 101L;

        when(reviewService.authToWriteReview(eq(bookId), any(HttpServletRequest.class))).thenReturn(orderId);

        mockMvc.perform(get("/shop/members/reviews/{book-id}", bookId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("bookId"))
            .andExpect(model().attribute("bookId", bookId))
            .andExpect(model().attributeExists("orderId"))
            .andExpect(model().attribute("orderId", orderId))
            .andExpect(view().name("review-write"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testGetReviews() throws Exception {
        // Given: Mock MemberReviewResponse 데이터 생성
        List<MemberReviewResponse> reviews = List.of(
            createMemberReviewResponse(
                101L, 1L, "https://example.com/image.jpg", "Book Title", 5,
                "Amazing Book", "This is a great book!", "https://example.com/review-image.jpg"
            )
        );

        // Mock Service 동작 설정
        when(reviewService.getReviewsByMemberId(any(HttpServletRequest.class))).thenReturn(reviews);

        // When & Then
        mockMvc.perform(get("/members/my-page/reviews"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("reviews"))
            .andExpect(model().attribute("reviews", reviews))
            .andExpect(view().name("reviews"));
    }

    // MemberReviewResponse 객체를 생성하는 메서드
    private MemberReviewResponse createMemberReviewResponse(
        Long orderId, Long bookId, String bookImageUrl, String bookTitle,
        int reviewRate, String reviewTitle, String reviewContent, String reviewImageUrl) {

        MemberReviewResponse response = new MemberReviewResponse();
        response.setOrderId(orderId);
        response.setBookId(bookId);
        response.setBookImageUrl(bookImageUrl);
        response.setBookTitle(bookTitle);
        response.setReviewRate(reviewRate);
        response.setReviewTitle(reviewTitle);
        response.setReviewContent(reviewContent);
        response.setReviewImageUrl(reviewImageUrl);
        return response;
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testGetReview() throws Exception {
        Long orderId = 101L;
        Long bookId = 1L;

        ReviewResponse reviewResponse = new ReviewResponse(5,"Amazing read!", "review",null);

        when(reviewService.getReview(any(HttpServletRequest.class), eq(orderId), eq(bookId))).thenReturn(reviewResponse);

        mockMvc.perform(get("/shop/members/reviews/update/{order-id}/{book-id}", orderId, bookId))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("bookId"))
            .andExpect(model().attribute("bookId", bookId))
            .andExpect(model().attributeExists("orderId"))
            .andExpect(model().attribute("orderId", orderId))
            .andExpect(model().attributeExists("review"))
            .andExpect(model().attribute("review", reviewResponse))
            .andExpect(view().name("review-rewrite"));
    }
}