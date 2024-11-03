package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LoginFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LogoutIllegalAccessException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.MemberRegisterFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberAdapter memberAdapter;

    @Override
    public MessageResponse memberRegister(MemberRegisterRequest request) {
        ResponseEntity<MessageResponse> response = memberAdapter.memberRegister(request);

        try {
            if(response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new MemberRegisterFailedException("회원가입에 실패하였습니다.");
        }
        catch ( HttpClientErrorException  | HttpServerErrorException e ) {
            throw new MemberRegisterFailedException("회원가입에 실패하였습니다.");
        }

    }

    @Override
    public MessageResponse memberLogin(MemberLoginRequest requestBody, HttpServletResponse httpServletResponse) {
        ResponseEntity<MessageResponse> response = memberAdapter.memberLogin(requestBody);

        try {
            if(response.getStatusCode().is2xxSuccessful()) {
                List<String> cookies = response.getHeaders().get("Set-Cookie");
                httpServletResponse.addHeader("Set-Cookie", cookies.get(0));
                httpServletResponse.addHeader("Set-Cookie", cookies.get(1));
                return response.getBody();
            }

            throw new LoginFailedException("로그인에 실패하였습니다.");
        }

        catch ( HttpClientErrorException  | HttpServerErrorException e ) {
            throw new LoginFailedException("로그인에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse memberLogout() {
        ResponseEntity<MessageResponse> response = memberAdapter.memberLogout();

        try {

            if(response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new LogoutIllegalAccessException("잘못된 접근입니다.");
        }

        catch ( HttpClientErrorException  | HttpServerErrorException e ) {
            throw new LogoutIllegalAccessException("잘못된 접근입니다.");
        }


    }
}
