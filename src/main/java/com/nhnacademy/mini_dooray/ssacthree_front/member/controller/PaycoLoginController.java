package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.member.service.PaycoService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PaycoLoginController {


    private final PaycoService paycoService;


    @GetMapping("/payco-login")
    public String paycoLogin() {
        return paycoService.getAuthorizationCodeUrl();
    }

    @GetMapping("/members/payco-connection")
    public String paycoConnection() {
        return paycoService.getAuthorizationCodeForConnection();
    }

    @GetMapping("/payco/callback")
    public String paycoLoginCallback(@RequestParam(name = "code") String code,
        HttpServletResponse response) {
        // TODO : access-token 발급 받는거 부터 해야뎀.. ㅠ
        String accessToken = paycoService.getAccessToken(code);
        String paycoIdNo = paycoService.getPaycoIdNo(accessToken);
        paycoService.paycoLogin(paycoIdNo, response);
        return "redirect:/shop/members/carts";
    }

    @GetMapping("/members/payco-connection/callback")
    public String paycoConnectionCallback(@RequestParam(name = "code") String code, Model model,
        RedirectAttributes redirectAttributes) {
        String accessToken = paycoService.getAccessToken(code);
        String paycoIdNo = paycoService.getPaycoIdNo(accessToken);
        redirectAttributes.addAttribute("connectionResult", paycoService.paycoConnect(paycoIdNo));

        return "redirect:/members/my-page";
    }


}
