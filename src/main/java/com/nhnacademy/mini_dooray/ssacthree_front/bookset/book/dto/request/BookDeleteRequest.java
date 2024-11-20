package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class BookDeleteRequest {
    private Long bookId;
    private String bookName;
    private String bookInfo;
    private BookStatus bookStatus;

    @Setter
    private List<AuthorNameResponse> authors;

    public BookDeleteRequest(Long bookId, String bookName, String bookInfo) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookInfo = bookInfo;
        this.bookStatus = BookStatus.ON_SALE;
    }

}