package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service.DeliveryRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.PointSaveRuleCustomerService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.CookieUtil;
import com.nhnacademy.mini_dooray.ssacthree_front.review.dto.BookReviewResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final PointSaveRuleCustomerService pointSaveRuleCustomerService;

    @GetMapping("/books")
    public String getBooksByFilters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookName:asc") String[] sort,
            @RequestParam(name = "author-id", required = false) Long authorId,
            @RequestParam(name = "category-id", required = false) Long categoryId,
            @RequestParam(name = "tag-id", required = false) Long tagId,
            Model model,
            HttpServletRequest request) {

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

        if (CookieUtil.checkAccessTokenCookie(request)) {
            List<Long> likeBooks = bookCommonService.getLikedBooksIdForCurrentUser();
            model.addAttribute("likeBooks", likeBooks);
        }

        // 데이터 가져오기
        Page<BookListResponse> books = getBooksByFilter(page, size, sort, authorId, categoryId, tagId);

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

    private Page<BookListResponse> getBooksByFilter(
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
            Model model,
            HttpServletRequest request) {
        String[] sort = {"bookName"};
        Long authorId = 336L;

        if (CookieUtil.checkAccessTokenCookie(request)) {
            List<Long> likeBooks = bookCommonService.getLikedBooksIdForCurrentUser();
            model.addAttribute("likeBooks", likeBooks);
        }

        // 카테고리 정보 가져오기
        List<CategoryInfoResponse> rootCategories = categoryCommonService.getRootCategories();
        model.addAttribute("rootCategories", rootCategories);

        BookInfoResponse banner1 = bookCommonService.getBookById(483L); // <소년이 온다> 아이디

        // 조회수 많은 하나
        String[] viewSort = {"bookViewCount:desc"};
        Page<BookListResponse> banner2Page = bookCommonService.getAllAvailableBooks(page, 1, viewSort);
        BookListResponse banner2 = banner2Page.getContent().isEmpty() ? null : banner2Page.getContent().get(0);

        Page<BookListResponse> awardBooks = bookCommonService.getBooksByAuthorId(page, size, sort, authorId); // 한강 작가


        model.addAttribute("banner1", banner1);
        model.addAttribute("banner2", banner2);
        model.addAttribute("books", awardBooks);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", awardBooks.getTotalPages());

        return "index";
    }

    @GetMapping("/books/{book-id}")
    public String showBook(@PathVariable("book-id") Long bookId,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           Model model,
                           HttpServletRequest request) {
        String[] sort = {"reviewCreatedAt:desc"};
        model.addAttribute("book", bookCommonService.getBookById(bookId));

        List<CategoryNameResponse> categories = bookCommonService.getCategoriesByBookId(bookId);
        List<List<CategoryInfoResponse>> categoryPaths = new ArrayList<>();
        for (CategoryNameResponse category : categories) {
            categoryPaths.add(categoryCommonService.getCategoryPath(category.getCategoryId()));
        }

        DeliveryRuleGetResponse deliveryRule = deliveryRuleService.getCurrentDeliveryRule();
        model.addAttribute("deliveryRule", deliveryRule);

        if (CookieUtil.checkAccessTokenCookie(request)) {
            List<Long> likeBooks = bookCommonService.getLikedBooksIdForCurrentUser();
            model.addAttribute("likeBooks", likeBooks);
        }


        PointSaveRuleInfoResponse bookPointSaveRule = pointSaveRuleCustomerService.getBookPointSaveRule();
        model.addAttribute("bookPointSaveRule", bookPointSaveRule);

        Page<BookReviewResponse> reviews = reviewService.getReviewsByBookId(page, size, sort, bookId);
        model.addAttribute("reviews", reviews.getContent());
        model.addAttribute("paging", reviews); // Page 객체를 템플릿에 전달
        model.addAttribute("baseUrl", "/books/" + bookId); // 기본 URL
        model.addAttribute("categoryPaths", categoryPaths);
        return "bookDetails";
    }


}
