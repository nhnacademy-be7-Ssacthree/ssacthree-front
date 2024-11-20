package com.nhnacademy.mini_dooray.ssacthree_front.review.service;

import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface ReviewService {

    List<ReviewResponse> getReviewsByBookId(Long bookId);

    void postReviewBook(Long bookId,Long orderId,ReviewRequest reviewRequest, HttpServletRequest request);

    Long authToWriteReview(Long bookId, HttpServletRequest request);

}
