package com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.dto.AuthorUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.author.service.AuthorService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
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
    public String getAuthors(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "authorName:asc") String[] sort,
        Model model) {
        Map<String, Object> allParams = new HashMap<>();
        allParams.put("page", String.valueOf(page));
        allParams.put("size", String.valueOf(size));

        String extraParams = allParams.entrySet().stream()
            .filter(entry -> !"page".equals(entry.getKey()) && !"size".equals(entry.getKey()))
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining("&"));

        model.addAttribute("baseUrl", "/admin/authors");
        model.addAttribute("allParams", allParams);
        model.addAttribute("extraParams", extraParams);

        model.addAttribute("authors", authorService.getAllAuthors(page, size, sort));
        return "admin/author/authors";
    }

    @GetMapping("/create")
    public String createAuthor(){
        return "admin/author/createAuthor";
    }

    //기본 값 나오게...
    @GetMapping("/{authorId}")
    public String updateAuthorForm(@PathVariable long authorId, Model model) {
        AuthorUpdateRequest authorUpdateRequest = authorService.getAuthorById(authorId);
        model.addAttribute("authorUpdateRequest", authorUpdateRequest);
        return "admin/author/updateAuthor";
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
