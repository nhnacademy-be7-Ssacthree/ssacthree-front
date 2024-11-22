package com.nhnacademy.mini_dooray.ssacthree_front.review.service;

import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.MemberReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.BookReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface ReviewService {

    List<BookReviewResponse> getReviewsByBookId(Long bookId);

    void postReviewBook(Long bookId,Long orderId,ReviewRequest reviewRequest, HttpServletRequest request);

    Long authToWriteReview(Long bookId, HttpServletRequest request);

    List<MemberReviewResponse> getReviewsByMemberId(HttpServletRequest request);

    ReviewResponse getReview(HttpServletRequest request, Long orderId, Long bookId);
}
