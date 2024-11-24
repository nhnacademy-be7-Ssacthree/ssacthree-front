package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class BookSaveRequestMultipart {
    private Long bookId;
    private String bookName;
    private String bookIndex; // 목차
    private String bookInfo; // 책 설명
    private String bookIsbn;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;
    private int regularPrice; // 판매가
    private int salePrice; // 할인 가격
    private boolean isPacked;
    private int stock;
    private MultipartFile bookThumbnailImageUrl;
    private int bookViewCount;
    private int bookDiscount; // 할인율

    private String bookStatus; // 도서 상태

    // FK
    private Long publisherId;


    private List<Long> categoryIdList;

    private List<Long> authorIdList;

    private List<Long> tagIdList;

    public boolean getIsPacked() {
        return isPacked;
    }

    public void setIsPacked(boolean isPacked) {
        this.isPacked = isPacked;
    }
}
