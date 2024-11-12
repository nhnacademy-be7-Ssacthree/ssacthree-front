package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor // 써야 할 일이 있을 것 같아서 추가
@NoArgsConstructor
public class TagInfoResponse {
    Long tagId;
    String tagName;
}
