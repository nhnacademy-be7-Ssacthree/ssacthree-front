package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.CustomerNotFoundException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LoginFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LogoutIllegalAccessException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.MemberNotFoundException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.MemberRegisterFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
public class MemberServiceImpl implements MemberService {


    private final MemberAdapter memberAdapter;

    private static final String SET_COOKIE = "Set-Cookie";

    @Override
    public MessageResponse memberRegister(MemberRegisterRequest request) {
        ResponseEntity<MessageResponse> response = memberAdapter.memberRegister(request);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new MemberRegisterFailedException("회원가입에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException | FeignException e) {
            throw new MemberRegisterFailedException("회원가입에 실패하였습니다.");
        }

    }

    @Override
    public MessageResponse memberLogin(MemberLoginRequest requestBody,
        HttpServletResponse httpServletResponse) {
        ResponseEntity<MessageResponse> response = memberAdapter.memberLogin(requestBody);

        try {
            if (isHaveCookie(httpServletResponse, response)) {
                return response.getBody();
            }

            throw new LoginFailedException("로그인에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new LoginFailedException("로그인에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse memberLogout(HttpServletResponse httpServletResponse) {
        ResponseEntity<MessageResponse> response = memberAdapter.memberLogout();

        try {

            if (isHaveCookie(httpServletResponse, response)) {
                return response.getBody();
            }

            throw new LogoutIllegalAccessException("잘못된 접근입니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new LogoutIllegalAccessException("잘못된 접근입니다.");
        }
    }

    private boolean isHaveCookie(HttpServletResponse httpServletResponse,
        ResponseEntity<MessageResponse> response) {
        if (response.getStatusCode().is2xxSuccessful()) {
            List<String> cookies = response.getHeaders().get(SET_COOKIE);
            assert cookies != null;
            httpServletResponse.addHeader(SET_COOKIE, cookies.get(0));
            httpServletResponse.addHeader(SET_COOKIE, cookies.get(1));
            return true;
        }
        return false;
    }


    @Override
    public MemberInfoResponse getMemberInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access-token")) {
                accessToken = cookie.getValue();
                break;
            }
        }
        try {
            ResponseEntity<MemberInfoResponse> response = memberAdapter.memberInfo(
                "Bearer " + accessToken);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (FeignException e) {
            throw new CustomerNotFoundException("회원 정보를 불러올 수 없습니다.");
        }

        throw new RuntimeException("회원 정보를 불러올 수 없습니다.");
    }

    @Override
    public MessageResponse memberInfoUpdate(MemberInfoUpdateRequest requestBody,
        HttpServletRequest request) {

        String accessToken = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("access-token")) {
                accessToken = cookie.getValue();
            }
        }
        try {
            ResponseEntity<MessageResponse> response = memberAdapter.memberInfoUpdate(
                "Bearer " + accessToken, requestBody);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (FeignException e) {
            throw new RuntimeException("회원 수정이 불가합니다.");
        }

        throw new RuntimeException("회원 정보를 불러올 수 없습니다.");
    }


    @Override
    public MessageResponse memberWithdraw(HttpServletRequest request,
        HttpServletResponse response) {

        String accessToken = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals("access-token")) {
                accessToken = cookie.getValue();
            }
        }
        try {
            ResponseEntity<MessageResponse> feignResponse = memberAdapter.memberDelete(
                "Bearer " + accessToken);
            if (feignResponse.getStatusCode().is2xxSuccessful()) {
                
                memberAdapter.memberLogout();

                // 쿠키 터뜨려서 로그아웃
//                Cookie accessCookie = new Cookie("access-token", null);
//                Cookie refreshCookie = new Cookie("refresh-token", null);
//
//                accessCookie.setPath("/");
//                refreshCookie.setPath("/");
//
//                accessCookie.setMaxAge(0);
//                refreshCookie.setMaxAge(0);
//
//                response.addCookie(accessCookie);
//                response.addCookie(refreshCookie);
//
                return feignResponse.getBody();
            }
        } catch (FeignException e) {
            throw new MemberNotFoundException("회원을 찾을 수 없습니다.");
        }
        throw new MemberNotFoundException("회원을 찾을 수 없습니다.");
    }
}
