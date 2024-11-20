package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookCommonService {
    Page<BookInfoResponse> getBooksByAuthorId(int page, int size, String[] sort, Long authorId);

    Page<BookInfoResponse> getBooksByCategoryId(int page, int size, String[] sort, Long categoryId);

    Page<BookInfoResponse> getBooksByTagId(int page, int size, String[] sort, Long tagId);

    BookInfoResponse getBookById(Long bookId);

    List<CategoryNameResponse> getCategoriesByBookId(Long bookId);

    Page<BookInfoResponse> getAllAvailableBooks(int page, int size, String[] sort);
}
