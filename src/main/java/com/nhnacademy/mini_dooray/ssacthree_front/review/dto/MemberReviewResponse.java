package com.nhnacademy.mini_dooray.ssacthree_front.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberReviewResponse {

    private Long orderId;
    private Long bookId;
    private String bookImageUrl;
    private String bookTitle;
    private int reviewRate;
    private String reviewTitle;
    private String reviewContent;
    private String reviewImageUrl;
}
