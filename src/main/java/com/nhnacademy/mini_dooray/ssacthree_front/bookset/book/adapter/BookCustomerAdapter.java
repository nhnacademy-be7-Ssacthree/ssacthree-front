package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.request.BookLikeRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.response.BookLikeResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "bookCustomerClient")
public interface BookCustomerAdapter {
    @GetMapping("/shop/books")
    ResponseEntity<Page<BookInfoResponse>> getAllAvailableBooks(@RequestParam("page") int page,
                                                                @RequestParam("size") int size,
                                                                @RequestParam("sort") String[] sort);

    @GetMapping("/shop/books/authors/{author-id}")
    ResponseEntity<Page<BookInfoResponse>> getBooksByAuthorId(@RequestParam("page") int page,
                                                              @RequestParam("size") int size,
                                                              @RequestParam("sort") String[] sort,
                                                              @PathVariable("author-id") Long authorId);

    @GetMapping("/shop/books/categories/{category-id}")
    ResponseEntity<Page<BookInfoResponse>> getBooksByCategoryId(@RequestParam("page") int page,
                                                                @RequestParam("size") int size,
                                                                @RequestParam("sort") String[] sort,
                                                                @PathVariable("category-id") Long categoryId);

    @GetMapping("/shop/books/tags/{tag-id}")
    ResponseEntity<Page<BookInfoResponse>> getBooksByTagId(@RequestParam("page") int page,
                                                           @RequestParam("size") int size,
                                                           @RequestParam("sort") String[] sort,
                                                           @PathVariable("tag-id") Long tagId);

    @GetMapping("/shop/books/{book-id}")
    ResponseEntity<BookInfoResponse> getBookById(@PathVariable("book-id") Long bookId);

    @GetMapping("/shop/books/{book-id}/categories")
    ResponseEntity<List<CategoryNameResponse>> getCategoriesByBookId(@PathVariable("book-id") Long bookId);

    @GetMapping("/shop/books/likes")
    ResponseEntity<Page<BookInfoResponse>> getBooksByMemberId(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "bookName:asc") String[] sort,
                                                              @RequestParam(name = "member-id") Long memberId);

    @PostMapping("/shop/books/likes")
    ResponseEntity<BookLikeResponse> createBookLikeByMemberId(@RequestBody BookLikeRequest request);

    @DeleteMapping("/shop/books/likes/{book-id}/{member-id}")
    ResponseEntity<Boolean> deleteBookLikeByMemberId(@PathVariable(name = "book-id") Long bookId,
                                                     @PathVariable(name = "member-id") Long memberId);
}

