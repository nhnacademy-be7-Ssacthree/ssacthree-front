package com.nhnacademy.mini_dooray.ssacthree_front.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookOrderRequest {

    private Long bookId;
    private String bookName;
    private int regularPrice; // 판매가
    private int salePrice; // 할인 가격
    private int bookDiscount; // 할인율
    private boolean isPacked;
    private int stock;
    private String bookThumbnailImageUrl;
}
