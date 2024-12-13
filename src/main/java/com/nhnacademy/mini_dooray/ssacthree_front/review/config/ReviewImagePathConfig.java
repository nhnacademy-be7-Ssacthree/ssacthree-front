package com.nhnacademy.mini_dooray.ssacthree_front.review.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ReviewImagePathConfig {
    @Value("${review.image.path}")
    private String imagePath;

}