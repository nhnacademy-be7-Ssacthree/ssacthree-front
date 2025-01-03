package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookSearchResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "gateway-service", url = "${admin-client.url}", contextId = "bookMgmtClient")
public interface BookMgmtAdapter {

    @GetMapping("/books")
    ResponseEntity<Page<BookSearchResponse>> getAllBooks(
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("sort") String[] sort);

    @PostMapping("/books")
    ResponseEntity<MessageResponse> createBook(@RequestBody BookSaveRequest bookSaveRequest);

    @PutMapping("/books")
    ResponseEntity<MessageResponse> updateBook(@RequestBody BookUpdateRequest bookUpdateRequest);

    @DeleteMapping("/books/{book-id}")
    ResponseEntity<MessageResponse> deleteBook(@PathVariable(name = "book-id") Long bookId);

    @GetMapping("/books/{book-id}")
    ResponseEntity<BookInfoResponse> getBookByBookId(@PathVariable(name = "book-id") Long bookId);

}
