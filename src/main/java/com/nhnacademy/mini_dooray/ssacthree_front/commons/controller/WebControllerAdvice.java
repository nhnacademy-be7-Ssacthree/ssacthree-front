package com.nhnacademy.mini_dooray.ssacthree_front.commons.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LoginFailedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class WebControllerAdvice {

    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model) {
        model.addAttribute("exception", e);
        return "error";
    }

    @ExceptionHandler(LoginFailedException.class)
    public String handleLoginFailedException(LoginFailedException e,
        RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("loginFailedMessage", e.getMessage());
        return "redirect:/";
    }

}
