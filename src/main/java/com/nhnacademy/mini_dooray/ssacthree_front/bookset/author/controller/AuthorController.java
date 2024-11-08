package com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.service.AuthorService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/authors")
public class AuthorController {

    private final AuthorService authorService;

    private static final String REDIRECT_ADDRESS = "redirect:/admin/authors";

    @GetMapping
    public String getAuthors(Model model){
        model.addAttribute("authors", authorService.getAllAuthors());
        return "authors";
    }

    @GetMapping("/create")
    public String createAuthor(){
        return "createAuthor";
    }

    //기본 값 나오게...
    @GetMapping("/{authorId}")
    public String updateAuthorForm(@PathVariable long authorId, Model model) {
        AuthorUpdateRequest authorUpdateRequest = authorService.getAuthorById(authorId);
        model.addAttribute("authorUpdateRequest", authorUpdateRequest);
        return "updateAuthor";
     }

    @PostMapping("/create")
    public String createAuthor(@Valid @ModelAttribute AuthorCreateRequest authorCreateRequest,
                               BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }

        authorService.createAuthor(authorCreateRequest);

        return REDIRECT_ADDRESS;
    }

    @PostMapping
    public String updateAuthor(@Valid @ModelAttribute AuthorUpdateRequest authorUpdateRequest,
                               BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            throw new ValidationFailedException(bindingResult);
        }

        authorService.updateAuthor(authorUpdateRequest);
        return REDIRECT_ADDRESS;
    }

    @PostMapping("/{authorId}")
    public String deleteAuthor(@PathVariable(name = "authorId") Long authorId) {
        authorService.deleteAuthor(authorId);
        return REDIRECT_ADDRESS;
    }

}
