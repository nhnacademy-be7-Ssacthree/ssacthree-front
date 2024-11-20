package com.nhnacademy.mini_dooray.ssacthree_front.review.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.review.adapter.ReviewAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.service.ReviewService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewAdapter adapter;

    @Override
    public List<ReviewResponse> getReviewsByBookId(Long bookId) {
        try {
            ResponseEntity<List<ReviewResponse>> responseEntity = adapter.getReviewsByBookId(bookId);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                throw new RuntimeException("API 호출 실패: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("API 호출 중 예외 발생", e);
        }
    }
}
