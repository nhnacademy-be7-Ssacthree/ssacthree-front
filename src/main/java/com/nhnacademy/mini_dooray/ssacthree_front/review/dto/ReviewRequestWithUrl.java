package com.nhnacademy.mini_dooray.ssacthree_front.review.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ReviewRequestWithUrl {

    private int reviewRate;
    private String reviewTitle;
    private String reviewContent;
    private String reviewImageUrl;

}
