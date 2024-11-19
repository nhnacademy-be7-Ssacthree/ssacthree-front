package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class BookCustomerController {

    private final BookCommonService bookCommonService;
    private final CategoryCommonService categoryCommonService;

    @GetMapping("/books")
    public String getBooksByAuthorId(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookName:asc") String[] sort,
            @RequestParam(name = "author-id", required = false) Long authorId,
            Model model) {

        List<CategoryInfoResponse> rootCategories = categoryCommonService.getRootCategories();
        model.addAttribute("rootCategories", rootCategories);

        Map<String, Object> allParams = new HashMap<>();
        allParams.put("page", String.valueOf(page));
        allParams.put("size", String.valueOf(size));

        if (authorId != null) {
            allParams.put("author-id", authorId);

            Page<BookInfoResponse> books = bookCommonService.getBooksByAuthorId(page, size, sort, authorId);
            model.addAttribute("books", books);
            model.addAttribute("authorId", authorId);
        } else {
            Page<BookInfoResponse> books = bookCommonService.getAllAvailableBooks(page, size, sort);
            model.addAttribute("books", books);
        }

        String extraParams = allParams.entrySet().stream()
                .filter(entry -> !"page".equals(entry.getKey()) && !"size".equals(entry.getKey()))
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        model.addAttribute("baseUrl", "/books");
        model.addAttribute("allParams", allParams);
        model.addAttribute("extraParams", extraParams);

        return "bookList";
    }

    @GetMapping
    public String showAwardBook(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        String[] sort = {"bookName"};
        Long authorId = 354L;

        Page<BookInfoResponse> books = bookCommonService.getBooksByAuthorId(page, size, sort, authorId);

        model.addAttribute("books", books);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", books.getTotalPages());

        return "index";
    }

    @GetMapping("/books/{book-id}")
    public String showBook(@PathVariable("book-id") Long bookId, Model model) {
        model.addAttribute("book", bookCommonService.getBookById(bookId));

        List<CategoryNameResponse> categories = bookCommonService.getCategoriesByBookId(bookId);
        List<List<CategoryInfoResponse>> categoryPaths = new ArrayList<>();
        for (CategoryNameResponse category : categories) {
            categoryPaths.add(categoryCommonService.getCategoryPath(category.getCategoryId()));
        }

        model.addAttribute("categoryPaths", categoryPaths);
        return "bookDetails";
    }

}
