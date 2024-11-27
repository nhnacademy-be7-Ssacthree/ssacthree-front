package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.request.BookLikeRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.booklike.dto.response.BookLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/shop/members/likes")
public class MemberLikeController {

    private final BookCommonService bookCommonService;

    @PostMapping
    public ResponseEntity<BookLikeResponse> createBookLikeByMemberId(@RequestBody BookLikeRequest request) {
        BookLikeResponse response = bookCommonService.createBookLikeByMemberId(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{book-id}")
    ResponseEntity<Boolean> deleteBookLikeByMemberId(@PathVariable(name = "book-id") Long bookId) {
        Boolean result = bookCommonService.deleteBookLikeByMemberId(bookId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
