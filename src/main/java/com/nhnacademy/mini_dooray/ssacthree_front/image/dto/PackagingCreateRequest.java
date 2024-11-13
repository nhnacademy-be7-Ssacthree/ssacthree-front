package com.nhnacademy.mini_dooray.ssacthree_front.image.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackagingCreateRequest {
    private String packagingName;
    private Long packagingPrice;
    private String imageUrl; // Added imageUrl field
}
