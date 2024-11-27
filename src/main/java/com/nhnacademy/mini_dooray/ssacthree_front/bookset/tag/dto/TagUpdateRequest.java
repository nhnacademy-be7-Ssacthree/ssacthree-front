package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagUpdateRequest {
    private Long tagId;
    private String tagName;
}
