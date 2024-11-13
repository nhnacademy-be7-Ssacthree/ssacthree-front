package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;

@Controller
@RequiredArgsConstructor
@RequestMapping()
public class BookCustomerController {

    private final BookCommonService bookCommonService;
    private final CartService cartService;

    @GetMapping("/author/{author-id}")
    public String getBooksByAuthorId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookName") String[] sort,
            @PathVariable("author-id") Long authorId,
            Model model) {

        Page<BookInfoResponse> books = bookCommonService.getBooksByAuthorId(page, size, sort, authorId);

        model.addAttribute("books", books.getContent()); // 실제 데이터 리스트
        model.addAttribute("totalPages", books.getTotalPages()); // 전체 페이지 수
        model.addAttribute("currentPage", books.getNumber()); // 현재 페이지
        model.addAttribute("pageSize", books.getSize()); // 페이지 크기
        model.addAttribute("authorId", authorId); // authorId 값

        return "bookLists"; // bookList.html로 데이터를 전달
    }

    @GetMapping("/")
    public String showBestSelling(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model,
            HttpServletRequest request) {
        String[] sort = {"bookName"};
        Long authorId = 365L;

        Page<BookInfoResponse> books = bookCommonService.getBooksByAuthorId(page, size, sort, authorId);
        model.addAttribute("books", books.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", books.getTotalPages());

        return "index";
    }

    @GetMapping("/books/{book-id}")
    public String showBook(@PathVariable("book-id") Long bookId, Model model) {
        model.addAttribute("book", bookCommonService.getBookById(bookId));
        return "bookDetails";
    }

}
