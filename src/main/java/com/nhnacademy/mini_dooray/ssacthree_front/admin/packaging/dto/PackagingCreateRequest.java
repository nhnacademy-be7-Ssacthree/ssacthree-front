package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PackagingCreateRequest {

    private String name;

    private int price;

    private String imageUrl;
}
