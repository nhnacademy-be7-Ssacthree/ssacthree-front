package com.nhnacademy.mini_dooray.ssacthree_front.review.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.image.adapter.ImageUploadAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.AddAddressFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.review.adapter.ReviewAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewRequestWithUrl;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.exception.PostReviewFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.review.exception.UnauthorizedReviewException;
import com.nhnacademy.mini_dooray.ssacthree_front.review.service.ReviewService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewAdapter adapter;
    private static final String IMAGE_PATH = "/ssacthree/review/";
    private final ImageUploadAdapter imageUploadAdapter;

    private static final String BEARER = "Bearer ";

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

    @Override
    public void postReviewBook(Long bookId,Long orderId,ReviewRequest reviewRequest, HttpServletRequest request) {
        String accessToken = getAccessToken(request);

        String imageurl = imageUploadAdapter.uploadImage(reviewRequest.getReviewImage(),IMAGE_PATH);

        ReviewRequestWithUrl requestWithUrl = new ReviewRequestWithUrl(reviewRequest.getReviewRate(),reviewRequest.getReviewTitle(),reviewRequest.getReviewContent(),imageurl);

        try {
            ResponseEntity<Void> response = adapter.postReviewBook(
                BEARER + accessToken,bookId,orderId,requestWithUrl);

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PostReviewFailedException("리뷰 등록에 실패하였습니다.");
        }
    }

    @Override
    public Long authToWriteReview(Long bookId, HttpServletRequest request) {
        String accessToken = getAccessToken(request);

        try {
            ResponseEntity<Long> response = adapter.authToWriteReview(
                BEARER + accessToken,bookId);

            // 상태 코드에 따른 분기 처리
            if (response.getStatusCode() == HttpStatus.OK) {
                // 리뷰 작성 권한 있음
                response.getBody();
                return response.getBody();
            } else if (response.getStatusCode() == HttpStatus.FORBIDDEN) {
                // 리뷰 작성 권한 없음
                throw new UnauthorizedReviewException("리뷰 작성 권한이 없습니다.");
            } else {
                // 예상하지 못한 응답 처리
                throw new RuntimeException("예상치 못한 상태 코드: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PostReviewFailedException("리뷰 등록에 실패하였습니다.");
        }
    }


    public String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access-token")) {
                accessToken = cookie.getValue();
                break;
            }
        }
        return accessToken;
    }


}
