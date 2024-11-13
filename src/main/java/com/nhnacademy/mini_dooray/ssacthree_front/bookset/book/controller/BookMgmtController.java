package com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.controller;


import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.service.AuthorService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.request.BookSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.exception.BookFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.category.service.CategoryCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.service.PublisherMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.TagMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/books")
public class BookMgmtController {

    private final BookMgmtService bookMgmtService;
    private final AuthorService authorService;
    private final CategoryCommonService categoryCommonService;
    private final PublisherMgmtService publisherMgmtService;
    private final TagMgmtService tagMgmtService;

    private static final String REDIRECT_ADDRESS = "redirect:/admin/books";

    @GetMapping
    public String getBooks(Model model) {
//       model.addAttribute("books", bookMgmtService.getAllBooks());
//       model.addAttribute("authors", authorService.getAllAuthors());
//       model.addAttribute("categories", categoryCommonService.getAllCategories());
//       model.addAttribute("publishers", publisherMgmtService.getAllPublishers());
//       model.addAttribute("tags", tagMgmtService.getAllTags());
        model.addAttribute("books", bookMgmtService.getAllBooks());
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("categories", categoryCommonService.getAllCategories());
        model.addAttribute("publishers", publisherMgmtService.getAllPublishers());
        model.addAttribute("tags", tagMgmtService.getAllTags());
        return "admin/book/books";
    }

    @GetMapping("/create")
    public String createBook(Model model){
        model.addAttribute("bookSaveRequest", new BookSaveRequest()); // 빈 폼 객체 추가
//        model.addAttribute("authors", authorService.getAllAuthors());
//        model.addAttribute("categories", categoryCommonService.getAllCategories());
//        model.addAttribute("publishers", publisherMgmtService.getAllPublishers());
//        model.addAttribute("tags", tagMgmtService.getAllTags());

        model.addAttribute("authors", null);
        model.addAttribute("categories", null);
        model.addAttribute("publishers", null);
        model.addAttribute("tags", null);
        return "admin/book/createBook";
    }

    @PostMapping("/create")
    public String createBook(@Valid @ModelAttribute BookSaveRequest bookSaveRequest,
                               BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("categories", categoryCommonService.getAllCategories());
            model.addAttribute("publishers", publisherMgmtService.getAllPublishers());
            model.addAttribute("tags", tagMgmtService.getAllTags());
            throw new ValidationFailedException(bindingResult);
        }

        bookMgmtService.createBook(bookSaveRequest);

        return REDIRECT_ADDRESS;
    }

    @PostMapping("/{book-id}")
    public String updateBook(@Valid @ModelAttribute BookSaveRequest bookSaveRequest, @PathVariable(name = "book-id") Long bookId,
                             Model model, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new BookFailedException("오류 발생");
        }
        bookMgmtService.updateBook(bookId, bookSaveRequest);
        return REDIRECT_ADDRESS;
    }

}
