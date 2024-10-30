package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LoginFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.MemberRegisterFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
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
    public MessageResponse memberLogin(MemberLoginRequest request) {
        ResponseEntity<MessageResponse> response = memberAdapter.memberLogin(request);
        try {
            if(response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new LoginFailedException("로그인에 실패하였습니다.");
        }
        catch ( HttpClientErrorException  | HttpServerErrorException e ) {
            throw new LoginFailedException("로그인에 실패하였습니다.");
        }
    }
}