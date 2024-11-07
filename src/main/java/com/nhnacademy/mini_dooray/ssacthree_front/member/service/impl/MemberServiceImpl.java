package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LoginFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LogoutIllegalAccessException;
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

    @Override
    public MessageResponse memberRegister(MemberRegisterRequest request) {
        ResponseEntity<MessageResponse> response = memberAdapter.memberRegister(request);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new MemberRegisterFailedException("회원가입에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new MemberRegisterFailedException("회원가입에 실패하였습니다.");
        }

    }

    @Override
    public MessageResponse memberLogin(MemberLoginRequest requestBody,
        HttpServletResponse httpServletResponse) {
        ResponseEntity<MessageResponse> response = memberAdapter.memberLogin(requestBody);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                List<String> cookies = response.getHeaders().get("Set-Cookie");
                httpServletResponse.addHeader("Set-Cookie", cookies.get(0));
                httpServletResponse.addHeader("Set-Cookie", cookies.get(1));
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

            if (response.getStatusCode().is2xxSuccessful()) {
                List<String> cookies = response.getHeaders().get("Set-Cookie");
                httpServletResponse.addHeader("Set-Cookie", cookies.get(0));
                httpServletResponse.addHeader("Set-Cookie", cookies.get(1));
                return response.getBody();
            }

            throw new LogoutIllegalAccessException("잘못된 접근입니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new LogoutIllegalAccessException("잘못된 접근입니다.");
        }
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
            throw new RuntimeException("회원 정보를 불러올 수 없습니다.");
        }

        return null;
    }
}
