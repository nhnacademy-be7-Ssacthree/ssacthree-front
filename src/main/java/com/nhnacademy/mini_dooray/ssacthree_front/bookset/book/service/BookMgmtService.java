package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookDeleteRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookMgmtService {

    Page<BookSearchResponse> getAllBooks();

    MessageResponse createBook(BookSaveRequest bookSaveRequest);

    MessageResponse updateBook(BookSaveRequest bookSaveRequest);

    MessageResponse deleteBook(Long bookId);

    BookInfoResponse getBookById(Long bookId);

}
