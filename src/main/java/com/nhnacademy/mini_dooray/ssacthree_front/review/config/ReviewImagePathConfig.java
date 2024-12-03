package com.nhnacademy.mini_dooray.ssacthree_front.review.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReviewImagePathConfig {
    @Value("${review.image.path}")
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }
}