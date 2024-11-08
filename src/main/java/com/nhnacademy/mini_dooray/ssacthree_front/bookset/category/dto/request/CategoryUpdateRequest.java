package com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryUpdateRequest {
    private String categoryName;

    private Long superCategoryId;

}
