package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum BookStatus {
    ON_SALE("판매 중"),
    NO_STOCK("재고 없음"),
    DISCONTINUED("판매 중단"),
    DELETE_BOOK("삭제 도서");

    private final String status;

    BookStatus(String status) {
        this.status = status;
    }

    public static BookStatus getBookStatus(final String status) {
        return Arrays.stream(BookStatus.values())
            .filter(bookStatus -> bookStatus.getStatus().equals(status))
            .findFirst()
            //TODO 에러 페이지로 에러 처리 해줘야함.
            .orElseThrow(() -> new IllegalArgumentException("잘못된 bookStatus, BookStatus Enum 에러: " + status));
    }

    public String getStatusName() {
        return status;
    }


}
