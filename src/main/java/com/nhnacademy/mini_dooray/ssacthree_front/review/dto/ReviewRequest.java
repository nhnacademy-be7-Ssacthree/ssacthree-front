package com.nhnacademy.mini_dooray.ssacthree_front.review.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
public class ReviewRequest {

    private int reviewRate;
    private String reviewTitle;
    private String reviewContent;
    private MultipartFile reviewImage;

}
