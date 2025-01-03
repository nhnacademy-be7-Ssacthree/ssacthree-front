package com.nhnacademy.mini_dooray.ssacthree_front.review.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.MemberReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewRequestWithUrl;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.BookReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "reviewClient")
public interface ReviewAdapter {

    @GetMapping("shop/books/reviews/{book-id}")
    ResponseEntity<Page<BookReviewResponse>> getReviewsByBookId(@RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("sort") String[] sort,
        @PathVariable("book-id") Long bookId);

    @GetMapping("shop/members/reviews/{book-id}") //권한 조회
    ResponseEntity<Long> authToWriteReview(@RequestHeader("Authorization") String authorizationHeader,@PathVariable("book-id") Long bookId);

    @PostMapping("shop/members/reviews")
    ResponseEntity<Void> postReviewBook(@RequestHeader("Authorization") String authorizationHeader,@RequestParam("book-id") Long bookId,@RequestParam("order-id") Long orderId,@RequestBody ReviewRequestWithUrl reviewRequestWithUrl);

    @GetMapping("shop/members/reviews")
    ResponseEntity<List<MemberReviewResponse>> getReviewsByMemberId(@RequestHeader("Authorization") String authorizationHeader);

    @GetMapping("shop/members/reviews/update/{order-id}/{book-id}")
    ResponseEntity<ReviewResponse> getReview(@RequestHeader("Authorization") String authorizationHeader,@PathVariable("order-id") Long orderId,@PathVariable("book-id") Long bookId);
}
