package com.nhnacademy.mini_dooray.ssacthree_front.review.service;

import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewResponse;
import java.util.List;

public interface ReviewService {

    List<ReviewResponse> getReviewsByBookId(Long bookId);

}
