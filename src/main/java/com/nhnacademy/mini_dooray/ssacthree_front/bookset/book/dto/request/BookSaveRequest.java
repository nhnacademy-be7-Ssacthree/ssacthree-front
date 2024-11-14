package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BookSaveRequest {
    private String bookName;
    private String bookIndex; // 목차
    private String bookInfo; // 책 설명
    private String bookIsbn;
//    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
//    private LocalDateTime publicationDate;
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

//    private String bookStatus; // 도서 상태

    // FK
    private Long publisherId;


    private List<Long> categoryIdList;

    private List<Long> authorIdList;

    private List<Long> tagIdList;

    public boolean getIsPacked() {
        return isPacked;
    }
}
