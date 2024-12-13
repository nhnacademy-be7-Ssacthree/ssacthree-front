package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookSaveRequest {
    private Long bookId;
    private String bookName;
    private String bookIndex; // 목차
    private String bookInfo; // 책 설명
    private String bookIsbn;
    // LocalDate로 수정하여 날짜만 받음
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;
    private int regularPrice; // 판매가
    private int salePrice; // 할인 가격
    private boolean isPacked;
    private int stock;
    private String bookThumbnailImageUrl;
    private int bookViewCount;
    private int bookDiscount; // 할인율

    private String bookStatus; // 도서 상태

    // FK
    private Long publisherId;

    private List<Long> categoryIdList = new ArrayList<>();

    private List<Long> authorIdList = new ArrayList<>();

    private List<Long> tagIdList = new ArrayList<>();

    public boolean getIsPacked() {
        return isPacked;
    }

    public void setIsPacked(boolean isPacked) {
        this.isPacked = isPacked;
    }
}
