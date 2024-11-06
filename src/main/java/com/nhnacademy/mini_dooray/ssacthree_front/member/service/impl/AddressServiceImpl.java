package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.AddAddressFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.AddressFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.AddressService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final MemberAdapter memberAdapter;


    public AddressResponse addNewAddress(HttpServletRequest request, AddressRequest addressRequest) {
        // 쿠키에서 access-token 추출
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access-token")) {
                accessToken = cookie.getValue();
                break;
            }
        }

        if (accessToken == null) {
            throw new RuntimeException("Access token이 존재하지 않습니다.");
        }

        try {
            // Authorization 헤더에 Bearer 토큰 포함하여 요청 전송
            ResponseEntity<AddressResponse> response = memberAdapter.addNewAddress(
                "Bearer " + accessToken, addressRequest);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new AddAddressFailedException("주소 입력에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AddAddressFailedException("주소 입력에 실패하였습니다.");
        }
    }

    @Override
    public List<AddressResponse> getAddresses(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access-token")) {
                accessToken = cookie.getValue();
                break;
            }
        }

        if (accessToken == null) {
            throw new RuntimeException("Access token이 존재하지 않습니다.");
        }

        try {
            // Authorization 헤더에 Bearer 토큰 포함하여 요청 전송
            ResponseEntity<List<AddressResponse>> response = memberAdapter.getAddresses(
                "Bearer " + accessToken);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new AddressFailedException("주소 응답에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AddressFailedException("주소 응답에 실패하였습니다.");
        }
    }

    @Override
    public void deleteAddress(long addressId, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access-token")) {
                accessToken = cookie.getValue();
                break;
            }
        }

        if (accessToken == null) {
            throw new RuntimeException("Access token이 존재하지 않습니다.");
        }

        try {
            // Authorization 헤더에 Bearer 토큰 포함하여 요청 전송
            ResponseEntity<Void> response = memberAdapter.deleteAddress(
                "Bearer " + accessToken,addressId);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new AddressFailedException("주소 응답에 실패하였습니다.");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AddressFailedException("주소 응답에 실패하였습니다.");
        }

    }
}
