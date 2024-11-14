package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "bookCustomerClient")
public interface BookCustomerAdapter {
    @GetMapping("/shop/books")
    ResponseEntity<Page<BookInfoResponse>> getRecentBooks(@RequestParam("page") int page,
                                                          @RequestParam("size") int size,
                                                          @RequestParam("sort") String[] sort);

    @GetMapping("/shop/books/author/{author-id}")
    ResponseEntity<Page<BookInfoResponse>> getBooksByAuthorId(@RequestParam("page") int page,
                                                              @RequestParam("size") int size,
                                                              @RequestParam("sort") String[] sort,
                                                              @PathVariable("author-id") Long authorId);

    @GetMapping("/shop/books/{book-id}")
    ResponseEntity<BookInfoResponse> getBookById(@PathVariable("book-id") Long bookId);

    @GetMapping("/shop/books/{book-id}/categories")
    ResponseEntity<List<CategoryNameResponse>> getCategoriesByBookId(@PathVariable("book-id") Long bookId);
}

