package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.controller;


import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.service.AuthorService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookUpdateRequestMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponseMultipart;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.exception.BookFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryNameResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryAdminService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.service.PublisherMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.TagMgmtService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/books")
public class BookMgmtController {

    private final BookMgmtService bookMgmtService;
    private final AuthorService authorService;
    private final CategoryAdminService categoryAdminService;
    private final PublisherMgmtService publisherMgmtService;
    private final TagMgmtService tagMgmtService;

    private static final String REDIRECT_ADDRESS = "redirect:/admin/books";
    private static final String BOOK_UPDATE_ERROR_MESSAGE = "책 정보 수정 실패";
    private static final String BOOK_CREATE_ERROR_MESSAGE = "책 정보 생성 실패";

    @GetMapping
    public String getBooks(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "bookName:asc") String[] sort,
        Model model) {

        // 공통 필터 파라미터를 Map으로 정리
        Map<String, Object> allParams = new HashMap<>();
        allParams.put("page", page);
        allParams.put("size", size);
        allParams.put("sort", sort);

        String extraParams = allParams.entrySet().stream()
            .filter(entry -> !"page".equals(entry.getKey()) && !"size".equals(entry.getKey()) && !"sort".equals(entry.getKey()))
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));

        model.addAttribute("books", bookMgmtService.getAllBooks(page, size, sort));
        model.addAttribute("baseUrl", "/admin/books");
        model.addAttribute("extraParams", extraParams.isEmpty() ? "" : "&" + extraParams); // 항상 &로 시작
        model.addAttribute("sort", sort[0]); // 현재 선택된 정렬 방식 추가
        return "admin/book/books";
    }

    @GetMapping("/create")
    public String createBook(Model model){

        // 작가 목록 가져오기
        List<AuthorGetResponse> authors = authorService.getAllAuthorList();

        // 카테고리 목록 가져오기
        ResponseEntity<List<CategoryInfoResponse>> categoriesResponse = categoryAdminService.getAllCategoriesForAdmin();
        List<CategoryInfoResponse> categories = categoriesResponse.getBody();

        // 출판사 목록 가져오기
        List<PublisherGetResponse> publishers = publisherMgmtService.getAllPublisherList();

        // 태그 목록 가져오기
        List<TagInfoResponse> tags = tagMgmtService.getAllTagList();

        // 모델에 데이터 추가
        model.addAttribute("bookSaveRequestMultipart", new BookSaveRequestMultipart());
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        model.addAttribute("publishers", publishers);
        model.addAttribute("tags", tags);
        return "admin/book/createBook";
    }

    @GetMapping("/update/{book-id}")
    public String updateBookForm(@PathVariable(name = "book-id") Long bookId, Model model) {

        BookInfoResponse bookInfoResponse = bookMgmtService.getBookById(bookId);

        // BookSaveRequest 초기화
        BookUpdateRequestMultipart bookUpdateRequestMultipart = bookMgmtService.setBookUpdateRequestMultipart(bookInfoResponse);

        model.addAttribute("bookInfoResponse", bookInfoResponse);
        model.addAttribute("bookUpdateRequestMultipart", bookUpdateRequestMultipart);
        model.addAttribute("publishers", publisherMgmtService.getAllPublisherList());
        model.addAttribute("existingCategories",bookInfoResponse.getCategories());
        model.addAttribute("categories", categoryAdminService.getAllCategoriesForAdmin().getBody());
        model.addAttribute("tags", tagMgmtService.getAllTagList());
        model.addAttribute("existingTags",bookInfoResponse.getTags());
        model.addAttribute("authors", authorService.getAllAuthorList());
        model.addAttribute("existingAuthors",bookInfoResponse.getAuthors());
        return "admin/book/updateBook";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute BookSaveRequestMultipart bookSaveRequestMultipart,
        BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BookFailedException(BOOK_CREATE_ERROR_MESSAGE);
        }
        MultipartFile bookThumbnailUrlMultipartFile = bookSaveRequestMultipart.getBookThumbnailImageUrl();

        bookMgmtService.createBook(bookSaveRequestMultipart, bookThumbnailUrlMultipartFile);
        return REDIRECT_ADDRESS;
    }

    @PostMapping("/update")
    public String updateBook(@Valid @ModelAttribute BookUpdateRequestMultipart bookUpdateRequestMultipart,
        BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BookFailedException(BOOK_UPDATE_ERROR_MESSAGE);
        }

        bookMgmtService.updateBook(bookUpdateRequestMultipart, bookUpdateRequestMultipart.getBookThumbnailImageUrlMultipartFile());

        return REDIRECT_ADDRESS;
    }

    @PostMapping("/delete/{book-id}")
    public String deleteBook(@PathVariable(name = "book-id") Long bookId){
        bookMgmtService.deleteBook(bookId);

        return REDIRECT_ADDRESS;
    }

}