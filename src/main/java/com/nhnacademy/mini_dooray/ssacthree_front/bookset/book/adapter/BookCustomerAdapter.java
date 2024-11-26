package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookListResponse;
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
    ResponseEntity<Page<BookListResponse>> getAllAvailableBooks(@RequestParam("page") int page,
                                                                @RequestParam("size") int size,
                                                                @RequestParam("sort") String[] sort);

    @GetMapping("/shop/books/authors/{author-id}")
    ResponseEntity<Page<BookListResponse>> getBooksByAuthorId(@RequestParam("page") int page,
                                                              @RequestParam("size") int size,
                                                              @RequestParam("sort") String[] sort,
                                                              @PathVariable("author-id") Long authorId);

    @GetMapping("/shop/books/categories/{category-id}")
    ResponseEntity<Page<BookListResponse>> getBooksByCategoryId(@RequestParam("page") int page,
                                                                @RequestParam("size") int size,
                                                                @RequestParam("sort") String[] sort,
                                                                @PathVariable("category-id") Long categoryId);

    @GetMapping("/shop/books/tags/{tag-id}")
    ResponseEntity<Page<BookListResponse>> getBooksByTagId(@RequestParam("page") int page,
                                                           @RequestParam("size") int size,
                                                           @RequestParam("sort") String[] sort,
                                                           @PathVariable("tag-id") Long tagId);

    @GetMapping("/shop/books/{book-id}")
    ResponseEntity<BookInfoResponse> getBookById(@PathVariable("book-id") Long bookId);

    @GetMapping("/shop/books/{book-id}/categories")
    ResponseEntity<List<CategoryNameResponse>> getCategoriesByBookId(@PathVariable("book-id") Long bookId);

    @GetMapping("/shop/members/my-page/likes")
    ResponseEntity<Page<BookListResponse>> getBooksByMemberId(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "10") int size,
                                                              @RequestParam(defaultValue = "bookName:asc") String[] sort);

    @GetMapping("/shop/members/likeList")
    ResponseEntity<List<Long>> getLikedBooksIdForCurrentUser();

    @PostMapping("/shop/members/likes")
    ResponseEntity<BookLikeResponse> createBookLikeByMemberId(@RequestBody BookLikeRequest request);

    @DeleteMapping("/shop/members/likes/{book-id}")
    ResponseEntity<BookLikeResponse> deleteBookLikeByMemberId(@PathVariable(name = "book-id") Long bookId);
}

