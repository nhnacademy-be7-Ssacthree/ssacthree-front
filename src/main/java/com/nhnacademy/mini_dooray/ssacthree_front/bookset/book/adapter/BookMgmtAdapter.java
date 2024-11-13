package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="gateway-service", url = "http://localhost:8081/api", contextId = "bookMgmtClient")
public interface BookMgmtAdapter {

    @GetMapping("/shop/admin/books")
    ResponseEntity<List<BookInfoResponse>> getAllBooks();

    @PostMapping("/shop/admin/books")
    ResponseEntity<MessageResponse> createBook(@RequestBody BookSaveRequest bookSaveRequest);

    @PutMapping("/shop/admin/books/{book-id}")
    ResponseEntity<MessageResponse> updateBook(@RequestBody BookSaveRequest bookSaveRequest, @PathVariable(name = "book-id") Long bookId);

    @DeleteMapping("/shop/admin/books/{book-id}")
    ResponseEntity<MessageResponse> deleteBook(@PathVariable(name = "book-id") Long bookId);

    @GetMapping("/shop/admin/books/{book-id}")
    ResponseEntity<BookSaveRequest> getBookByBookId(@PathVariable(name = "book-id") Long bookId);

}
