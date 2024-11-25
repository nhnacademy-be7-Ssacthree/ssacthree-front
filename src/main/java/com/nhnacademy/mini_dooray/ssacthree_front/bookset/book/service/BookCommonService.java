package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.request.BookLikeRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.response.BookLikeResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookCommonService {
    Page<BookListResponse> getBooksByAuthorId(int page, int size, String[] sort, Long authorId);

    Page<BookListResponse> getBooksByCategoryId(int page, int size, String[] sort, Long categoryId);

    Page<BookListResponse> getBooksByTagId(int page, int size, String[] sort, Long tagId);

    BookInfoResponse getBookById(Long bookId);

    List<CategoryNameResponse> getCategoriesByBookId(Long bookId);

    Page<BookListResponse> getAllAvailableBooks(int page, int size, String[] sort);

    Page<BookListResponse> getBooksByMemberId(int page, int size, String[] sort);

    List<Long> getLikedBooksIdForCurrentUser();

    BookLikeResponse createBookLikeByMemberId(BookLikeRequest bookLikeRequest);

    BookLikeResponse deleteBookLikeByMemberId(Long bookId);
}
