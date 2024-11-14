package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.controller;


import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.service.AuthorService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.exception.BookFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.dto.response.CategoryInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryAdminService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.service.PublisherMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.TagMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String getBooks(Model model) {
        model.addAttribute("books", bookMgmtService.getAllBooks());
        return "admin/book/books";
    }

    @GetMapping("/create")
    public String createBook(Model model){

        // 작가 목록 가져오기
        List<AuthorGetResponse> authors = authorService.getAllAuthors();

        // 카테고리 목록 가져오기
        ResponseEntity<List<CategoryInfoResponse>> categoriesResponse = categoryAdminService.getAllCategoriesForAdmin();
        List<CategoryInfoResponse> categories = categoriesResponse.getBody();

        // 출판사 목록 가져오기
        List<PublisherGetResponse> publishers = publisherMgmtService.getAllPublishers();

        // 태그 목록 가져오기
        List<TagInfoResponse> tags = tagMgmtService.getAllTags();

        // 모델에 데이터 추가
        model.addAttribute("bookSaveRequest", new BookSaveRequest());
        model.addAttribute("authors", authors);
        model.addAttribute("categories", categories);
        model.addAttribute("publishers", publishers);
        model.addAttribute("tags", tags);
        return "admin/book/createBook";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute BookSaveRequest bookSaveRequest,
                               BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            throw new BookFailedException(BOOK_CREATE_ERROR_MESSAGE);
        }
        bookMgmtService.createBook(bookSaveRequest);
        return REDIRECT_ADDRESS;
    }

    @PostMapping("/{book-id}/update")
    public String updateBook(@Valid @ModelAttribute BookSaveRequest bookSaveRequest, @PathVariable(name = "book-id") Long bookId,
                             Model model, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BookFailedException(BOOK_UPDATE_ERROR_MESSAGE);
        }
        bookMgmtService.updateBook(bookId, bookSaveRequest);

        return REDIRECT_ADDRESS;
    }

    @PostMapping("/{book-id}/delete")
    public String deleteBook(@Valid @ModelAttribute BookSaveRequest bookSaveRequest, @PathVariable(name = "book-id") Long bookId,
                             Model model, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BookFailedException(BOOK_UPDATE_ERROR_MESSAGE);
        }
        bookMgmtService.deleteBook(bookId, bookSaveRequest);

        return REDIRECT_ADDRESS;
    }


}
