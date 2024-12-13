package com.nhnacademy.mini_dooray.ssacthree_front.review.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookReviewResponse {

    private String memberId;
    private int reviewRate;
    private String reviewTitle;
    private String reviewContent;
    private LocalDateTime reviewCreatedAt;
    private String reviewImage;

}
