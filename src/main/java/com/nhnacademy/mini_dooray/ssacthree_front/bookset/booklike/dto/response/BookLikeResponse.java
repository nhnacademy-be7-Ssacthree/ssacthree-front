package com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class BookLikeResponse {
    private Long bookId;
    private Long customerId;
    @Setter
    private Long likeCount;
}
