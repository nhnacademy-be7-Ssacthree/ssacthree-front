package com.nhnacademy.mini_dooray.ssacthree_front.review.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "reviewClient")
public interface ReviewAdapter {

    @GetMapping("shop/books/reviews/{book-id}")
    ResponseEntity<List<ReviewResponse>> getReviewsByBookId(@PathVariable("book-id") Long bookId);

}
