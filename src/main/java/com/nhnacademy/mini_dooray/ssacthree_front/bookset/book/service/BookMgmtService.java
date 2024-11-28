package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponseMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface BookMgmtService {

    Page<BookSearchResponse> getAllBooks(int page,int size,String[] sort);

    MessageResponse createBook(BookSaveRequestMultipart bookSaveRequestMultipart, MultipartFile bookThumbnailUrlMultipartFile);

    MessageResponse updateBook(BookUpdateRequestMultipart bookUpdateRequestMultipart, MultipartFile bookThumbnailUrlMultipartFile);

    MessageResponse deleteBook(Long bookId);

    BookInfoResponse getBookById(Long bookId);

    BookUpdateRequestMultipart setBookUpdateRequestMultipart(BookInfoResponse bookInfoResponse);

    String getImageUrl(MultipartFile bookThumbnailUrl);

}
