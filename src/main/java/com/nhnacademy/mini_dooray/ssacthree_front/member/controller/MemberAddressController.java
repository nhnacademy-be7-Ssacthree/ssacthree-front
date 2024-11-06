package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberAddressController {
    private final AddressService addressService;

    @PostMapping("/address")
    public String addNewAddress(@Valid @ModelAttribute AddressRequest addressRequest) {
        addressService.addNewAddress(addressRequest);  // API 서버로 요청 전달
        return "redirect:/my-page";
    }

    @GetMapping("/address") //주소 추가 페이지로 이동
    public String addressPage() {
        return "addressAdd";
    }
}
