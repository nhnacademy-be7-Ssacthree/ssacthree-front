package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;

import java.util.List;

public interface BookMgmtService {

    List<BookInfoResponse> getAllBooks();

    MessageResponse createBook(BookSaveRequest bookSaveRequest);

    MessageResponse updateBook(Long bookId, BookSaveRequest bookSaveRequest);

}
