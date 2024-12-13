package com.nhnacademy.mini_dooray.ssacthree_front.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ReviewResponse {

    private int reviewRate;
    private String reviewTitle;
    private String reviewContent;
    private String reviewImageUrl;
}
