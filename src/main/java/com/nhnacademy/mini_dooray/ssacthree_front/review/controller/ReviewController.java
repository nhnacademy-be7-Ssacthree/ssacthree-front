package com.nhnacademy.mini_dooray.ssacthree_front.review.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.MemberReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.exception.PostReviewFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.review.exception.UnauthorizedReviewException;
import com.nhnacademy.mini_dooray.ssacthree_front.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class ReviewController {

    private final ReviewService reviewService;


    @PostMapping("/shop/members/reviews")//리뷰 작성
    public String postReview(
        @RequestParam("bookId") Long bookId,
        @RequestParam("orderId") Long orderId,
        ReviewRequest reviewRequest, HttpServletRequest request) {

        reviewService.postReviewBook(bookId, orderId, reviewRequest, request);

        return "redirect:/members/my-page/reviews";
    }

    @GetMapping("/shop/members/reviews/{book-id}")
    public String authToWriteReview(@PathVariable("book-id") Long bookId, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) {

            // 리뷰 작성 권한 확인 로직
            Long orderId = reviewService.authToWriteReview(bookId, request);
            model.addAttribute("bookId", bookId);
            model.addAttribute("orderId", orderId);
            return "review-write"; // 리뷰 작성 페이지로 이동
    }

    @GetMapping("/members/my-page/reviews") //리뷰 리스트 조회
    public String getReviews(HttpServletRequest request, Model model) {

        List<MemberReviewResponse> reviews = reviewService.getReviewsByMemberId(request);

        model.addAttribute("reviews", reviews);

        return "reviews";
    }

    @GetMapping("/shop/members/reviews/update/{order-id}/{book-id}")
    public String getReview(HttpServletRequest request, @PathVariable("order-id") Long orderId,
        @PathVariable("book-id") Long bookId, Model model) {

        model.addAttribute("bookId", bookId);
        model.addAttribute("orderId", orderId);

        ReviewResponse reviewResponse = reviewService.getReview(request, orderId, bookId);

        model.addAttribute("review", reviewResponse);

        return "review-rewrite";
    }
}
