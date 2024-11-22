package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class BookListResponse {
    private Long bookId;
    private String bookName;
    private LocalDateTime publicationDate;
    private int regularPrice; // 판매가
    private int salePrice; // 할인 가격
    private String bookThumbnailImageUrl;
    private int bookViewCount;
    private int bookDiscount; // 할인율
    private String bookStatus; // 도서 상태

    private PublisherNameResponse publisher;

    @Setter
    private List<CategoryNameResponse> categories;

    @Setter
    private List<TagInfoResponse> tags;

    @Setter
    private List<AuthorNameResponse> authors;

    @Setter
    private Long likeCount; // 좋아요 수

    @Setter
    private Long reviewCount; // 리뷰 수

    @Setter
    private Double reviewRateAverage; // 리뷰 별점 평균
}
