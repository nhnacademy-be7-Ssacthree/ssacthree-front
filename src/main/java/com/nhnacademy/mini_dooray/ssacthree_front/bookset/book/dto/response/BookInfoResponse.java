package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookInfoResponse {
    private Long bookId;
    private String bookName;
    private String bookIndex; // 목차
    private String bookInfo; // 책 설명
    private String bookIsbn;
    private LocalDateTime publicationDate;
    private int regularPrice; // 판매가
    private int salePrice; // 할인 가격
    private boolean isPacked;
    private int stock;
    private String bookThumbnailImageUrl;
    private int bookViewCount;
    private int bookDiscount; // 할인율

    private String bookStatus; // 배송 상태

    // FK
    @Setter
    private PublisherNameResponse publisher;

    @Setter
    private List<CategoryNameResponse> categories;

    @Setter
    private List<TagInfoResponse> tags;

    @Setter
    private List<AuthorNameResponse> authors;

    public boolean isPacked() {
        return this.isPacked;
    }
}
