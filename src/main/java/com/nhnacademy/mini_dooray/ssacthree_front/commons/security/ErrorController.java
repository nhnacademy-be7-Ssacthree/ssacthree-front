package com.nhnacademy.mini_dooray.ssacthree_front.commons.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {

    private static final String EXCEPTION = "exception";

    @GetMapping("/unauthorized")
    public String handleUnauthorized(HttpServletRequest request, Model model) {
        Exception exception = (Exception) request.getAttribute(EXCEPTION);
        model.addAttribute(EXCEPTION, exception != null ? exception.getMessage() : "인증 실패");
        return "error";
    }

    @GetMapping("/forbidden")
    public String handleForbidden(HttpServletRequest request, Model model) {
        Exception exception = (Exception) request.getAttribute(EXCEPTION);
        model.addAttribute(EXCEPTION, exception != null ? exception.getMessage() : "권한 없음");
        return "error";
    }
}
