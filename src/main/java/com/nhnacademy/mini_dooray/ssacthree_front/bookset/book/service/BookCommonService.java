package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public interface BookCommonService {
    Page<BookInfoResponse> getBooksByAuthorId(int page, int size, String[] sort, Long authorId);

    BookInfoResponse getBookById(Long bookId);
}
