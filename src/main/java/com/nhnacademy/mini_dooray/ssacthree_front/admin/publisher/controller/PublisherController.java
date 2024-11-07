package com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.controller;


import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.service.PublisherService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/publishers")
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public String publisher(Model model) {
        model.addAttribute("publishers", publisherService.getAllPublishers());
        return "admin/publisher/publishers";
    }

    @PostMapping
    public String updatePublisher(@Valid @ModelAttribute PublisherUpdateRequest publisherUpdateRequest,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        publisherService.updatePublisher(publisherUpdateRequest);

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

        publisherService.createPublisher(publisherCreateRequest);

        return "redirect:/admin/publishers";
    }
}
