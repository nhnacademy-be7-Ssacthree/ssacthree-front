package com.nhnacademy.mini_dooray.ssacthree_front.review.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.image.adapter.ImageUploadAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.review.adapter.ReviewAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.MemberReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewRequestWithUrl;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.BookReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.exception.AuthToWriteFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.review.exception.GetReviewFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.review.exception.PostReviewFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.review.service.ReviewService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public Page<BookReviewResponse> getReviewsByBookId(int page, int size, String[] sort, Long bookId) {
        try {
            ResponseEntity<Page<BookReviewResponse>> responseEntity = adapter.getReviewsByBookId(page,size,sort,bookId);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                throw new GetReviewFailedException("책에 있는 리뷰를 달성하는데 실패하였습니다.");
            }
        } catch (Exception e) {
            throw new GetReviewFailedException("API 호출 중 예외 발생");
        }
    }

    @Override
    public void postReviewBook(Long bookId,Long orderId,ReviewRequest reviewRequest, HttpServletRequest request) {
        String accessToken = getAccessToken(request);

        String imageurl = imageUploadAdapter.uploadImage(reviewRequest.getReviewImage(),IMAGE_PATH);

        ReviewRequestWithUrl requestWithUrl = new ReviewRequestWithUrl(reviewRequest.getReviewRate(),reviewRequest.getReviewTitle(),reviewRequest.getReviewContent(),imageurl);

        try {
            adapter.postReviewBook(
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
            } else {
                throw new AuthToWriteFailedException("리뷰 권한이 없습니다.");
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PostReviewFailedException("리뷰 등록에 실패하였습니다.");
        }
    }

    @Override
    public List<MemberReviewResponse> getReviewsByMemberId(HttpServletRequest request) {
        String accessToken = getAccessToken(request);

        try {
            ResponseEntity<List<MemberReviewResponse>> response = adapter.getReviewsByMemberId(
                BEARER + accessToken);

            // 상태 코드에 따른 분기 처리
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                // 예상하지 못한 응답 처리
                throw new GetReviewFailedException("리뷰를 가져오는데 실패하였습니다.");
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PostReviewFailedException("리뷰 조회에 실패하였습니다.");
        }
    }

    @Override
    public ReviewResponse getReview(HttpServletRequest request, Long orderId, Long bookId) {
        String accessToken = getAccessToken(request);

        try {
            ResponseEntity<ReviewResponse> response = adapter.getReview(
                BEARER + accessToken,orderId,bookId);

            // 상태 코드에 따른 분기 처리
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            } else {
                // 예상하지 못한 응답 처리
                throw new GetReviewFailedException("리뷰를 가져오는데 실패하였습니다.");
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PostReviewFailedException("리뷰 조회에 실패하였습니다.");
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
