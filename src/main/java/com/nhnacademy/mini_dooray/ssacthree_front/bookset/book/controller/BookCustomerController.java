package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service.DeliveryRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.review.service.ReviewService;
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
    private final ReviewService reviewService;
    private final DeliveryRuleService deliveryRuleService;

    @GetMapping("/books")
    public String getBooksByFilters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookName:asc") String[] sort,
            @RequestParam(name = "author-id", required = false) Long authorId,
            @RequestParam(name = "category-id", required = false) Long categoryId,
            @RequestParam(name = "tag-id", required = false) Long tagId,
            Model model) {

        // 카테고리 정보 가져오기
        List<CategoryInfoResponse> rootCategories = categoryCommonService.getRootCategories();
        model.addAttribute("rootCategories", rootCategories);

        // 공통 필터 파라미터를 Map으로 정리
        Map<String, Object> allParams = new HashMap<>();
        allParams.put("page", page);
        allParams.put("size", size);
        allParams.put("sort", sort);

        // 추가 필터 파라미터 설정
        if (authorId != null) {
            allParams.put("author-id", authorId);
        }
        if (categoryId != null) {
            allParams.put("category-id", categoryId);
        }
        if (tagId != null) {
            allParams.put("tag-id", tagId);
        }

        // 데이터 가져오기
        Page<BookInfoResponse> books = getBooksByFilter(page, size, sort, authorId, categoryId, tagId);

        // `sort`를 제외한 추가 파라미터 문자열 생성
        String extraParams = allParams.entrySet().stream()
                .filter(entry -> !"page".equals(entry.getKey()) && !"size".equals(entry.getKey()) && !"sort".equals(entry.getKey()))
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        model.addAttribute("books", books);
        model.addAttribute("baseUrl", "/books");
        model.addAttribute("extraParams", extraParams.isEmpty() ? "" : "&" + extraParams); // 항상 &로 시작
        model.addAttribute("sort", sort[0]); // 현재 선택된 정렬 방식 추가

        return "bookList";
    }

    private Page<BookInfoResponse> getBooksByFilter(
            int page, int size, String[] sort,
            Long authorId, Long categoryId, Long tagId) {

        if (authorId != null) {
            return bookCommonService.getBooksByAuthorId(page, size, sort, authorId);
        } else if (categoryId != null) {
            return bookCommonService.getBooksByCategoryId(page, size, sort, categoryId);
        } else if (tagId != null) {
            return bookCommonService.getBooksByTagId(page, size, sort, tagId);
        } else {
            return bookCommonService.getAllAvailableBooks(page, size, sort);
        }
    }


    @GetMapping
    public String showHomeBook(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
        String[] sort = {"bookName"};
        Long authorId = 336L;

        BookInfoResponse banner1 = bookCommonService.getBookById(483L); // <소년이 온다> 아이디

        // 조회수 많은 하나
        String[] viewSort = {"bookViewCount"};
        Page<BookInfoResponse> banner2 = bookCommonService.getAllAvailableBooks(page, 1, viewSort);

        Page<BookInfoResponse> awardBooks = bookCommonService.getBooksByAuthorId(page, size, sort, authorId);

        model.addAttribute("banner1", banner1);
        model.addAttribute("banner2", banner2);
        model.addAttribute("books", awardBooks);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", awardBooks.getTotalPages());

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

//        DeliveryRuleGetResponse deliveryRule = deliveryRuleService.getCurrentDeliveryRule();
//        model.addAttribute("deliveryRule", deliveryRule);

        model.addAttribute("reviews", reviewService.getReviewsByBookId(bookId));
        model.addAttribute("categoryPaths", categoryPaths);
        return "bookDetails";
    }

    @GetMapping("/members/my-page/likes")
    public String showMyLikeBooks(int page, int size, String[] sort, Model model) {

        return "myLikes";
    }

}
