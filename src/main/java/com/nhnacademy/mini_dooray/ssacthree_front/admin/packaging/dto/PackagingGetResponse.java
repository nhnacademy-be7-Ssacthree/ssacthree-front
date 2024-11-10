package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PackagingGetResponse {
    private Long id;
    private String packagingName;
    private int packagingPrice;
    private String packagingImageUrl;
}
