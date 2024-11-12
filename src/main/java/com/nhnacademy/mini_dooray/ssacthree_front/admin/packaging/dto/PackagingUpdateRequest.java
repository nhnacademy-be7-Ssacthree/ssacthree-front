package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PackagingUpdateRequest {

    private String packagingName;

    private int packagingPrice;

    private String packagingImageUrl;
}
