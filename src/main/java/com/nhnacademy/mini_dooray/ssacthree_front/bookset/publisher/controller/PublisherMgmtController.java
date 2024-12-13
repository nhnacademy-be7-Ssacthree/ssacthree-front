package com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.controller;


import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.service.PublisherMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/publishers")
public class PublisherMgmtController {

    private final PublisherMgmtService publisherMgmtService;

    @GetMapping
    public String publisher(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "bookName:asc") String[] sort,
            Model model) {

        Map<String, Object> allParams = new HashMap<>();
        allParams.put("page", String.valueOf(page));
        allParams.put("size", String.valueOf(size));

        String extraParams = allParams.entrySet().stream()
                .filter(entry -> !"page".equals(entry.getKey()) && !"size".equals(entry.getKey()))
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        model.addAttribute("baseUrl", "/admin/publishers");
        model.addAttribute("allParams", allParams);
        model.addAttribute("extraParams", extraParams);

        model.addAttribute("publishers", publisherMgmtService.getAllPublishers(page, size, sort));
        return "admin/publisher/publishers";
    }

    @PostMapping
    public String updatePublisher(@Valid @ModelAttribute PublisherUpdateRequest publisherUpdateRequest,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        publisherMgmtService.updatePublisher(publisherUpdateRequest);

        return "redirect:/admin/publishers";
    }

    @GetMapping("/create")
    public String createPublisher() {
        return "admin/publisher/createPublisher";
    }

    @PostMapping("/create")
    public String createPublisher(@Valid @ModelAttribute PublisherCreateRequest publisherCreateRequest,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        publisherMgmtService.createPublisher(publisherCreateRequest);

        return "redirect:/admin/publishers";
    }
}
