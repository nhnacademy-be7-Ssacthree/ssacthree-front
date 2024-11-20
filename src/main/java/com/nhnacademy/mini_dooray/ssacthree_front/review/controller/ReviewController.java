package com.nhnacademy.mini_dooray.ssacthree_front.review.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.image.adapter.ImageUploadAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.ReviewRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("shop/members/reviews")
public class ReviewController {

    private final ReviewService reviewService;


    @PostMapping
    public String postReview(
        @RequestParam("bookId") Long bookId,
        @RequestParam("orderId") Long orderId,
        ReviewRequest reviewRequest, HttpServletRequest request) {

        reviewService.postReviewBook(bookId,orderId,reviewRequest,request);

        return "redirect:/";
    }

    @GetMapping("/{book-id}")
    public String authToWriteReview(@PathVariable("book-id") Long bookId, HttpServletRequest request, Model model) {

        Long orderId = reviewService.authToWriteReview(bookId,request);

        model.addAttribute("bookId",bookId);
        model.addAttribute("orderId",orderId);

        return "review-write";
    }
}
