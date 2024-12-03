package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop/books")
public class BookRestController {
    private final BookCommonService bookCommonService;

    @GetMapping("/categories/{category-id}")
    public ResponseEntity<Page<BookListResponse>> getBooksByCategoryId(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size,
                                                                       @RequestParam(defaultValue = "bookName:asc") String[] sort,
                                                                       @PathVariable(name = "category-id") Long categoryId) {
        String[] categorySort = {"publicationDate:desc", "bookViewCount:desc"};

        Page<BookListResponse> books = bookCommonService.getBooksByCategoryId(page, size, categorySort, categoryId);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Page<BookListResponse>> getAllBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookName:asc") String[] sort) {

        String[] categorySort = {"publicationDate:desc", "bookViewCount:desc"};

        Page<BookListResponse> books = bookCommonService.getAllAvailableBooks(page, size, categorySort);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
