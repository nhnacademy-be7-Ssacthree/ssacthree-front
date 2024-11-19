package com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthorGetResponse {
    private Long authorId;
    private String authorName;
    private String authorInfo;
}