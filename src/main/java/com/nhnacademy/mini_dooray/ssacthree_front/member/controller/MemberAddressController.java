package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.aop.annotation.LoginRequired;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class MemberAddressController {
    private final AddressService addressService;

    /**
     *  사용자 주소 추가
     * @param request 요청
     * @param addressRequest DTO
     * @return 사용자 주소 페이지로 리다이렉트
     */
    @LoginRequired
    @PostMapping("/address")
    public String addNewAddress(HttpServletRequest request, @ModelAttribute AddressRequest addressRequest) {
        addressService.addNewAddress(request, addressRequest);  // API 서버로 요청 전달
        return "redirect:/address-page";
    }

    /**
     *  사용자 주소 전체 조회
     * @param model html에 전달
     * @param request 요청
     * @return 주소 전체 전달
     */
    @LoginRequired
    @GetMapping("/address-page") //주소록 페이지로 이동
    public String addressPage(Model model, HttpServletRequest request) {
        List<AddressResponse> addresses = addressService.getAddresses(request);
        model.addAttribute("addresses", addresses);
        return "address";
    }

    /**
     *
     * @return 주소추가 html 이동
     */
    @GetMapping("/address") //주소 추가 페이지로 이동
    public String addressAdd() {
        return "addressAdd";
    }

    /**
     *
     * @param addressId 주소 아이디
     * @param request 요청
     * @return 해당 주소 삭제 처리
     */
    @PostMapping("/address/{id}") // 주소 삭제
    public String deleteAddress(@PathVariable("id") long addressId, HttpServletRequest request) {
        addressService.deleteAddress(addressId,request);
        return "redirect:/address-page";
    }
}
