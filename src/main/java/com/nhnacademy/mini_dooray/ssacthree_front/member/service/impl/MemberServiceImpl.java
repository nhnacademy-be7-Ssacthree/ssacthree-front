package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LoginFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LogoutIllegalAccessException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.MemberRegisterFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public MessageResponse memberLogout(HttpServletResponse httpServletResponse) {
        ResponseEntity<MessageResponse> response = memberAdapter.memberLogout();

        try {

            if(response.getStatusCode().is2xxSuccessful()) {
                List<String> cookies = response.getHeaders().get("Set-Cookie");
                httpServletResponse.addHeader("Set-Cookie", cookies.get(0));
                httpServletResponse.addHeader("Set-Cookie", cookies.get(1));
                return response.getBody();
            }

            throw new LogoutIllegalAccessException("잘못된 접근입니다.");
        }

        catch ( HttpClientErrorException  | HttpServerErrorException e ) {
            throw new LogoutIllegalAccessException("잘못된 접근입니다.");
        }
    }

    // TODO : 로그인 했는지 체크중이었는데 생각해보니깐... 로그인 체크 인터셉터 있잖아...??
    @Override
    public boolean isAuthenticated() {
        try {
            ResponseEntity<MessageResponse> response = memberAdapter.memberAuthenticate();
            if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                log.info("인증 실패: 로그인 필요");
                return false;
            } else if (response.getStatusCode() == HttpStatus.FORBIDDEN) {
                log.warn("권한 부족으로 접근 거부됨");
                return false;
            }
            return response.getStatusCode().is2xxSuccessful();
        } catch (FeignException e) {
            log.error("인증 인가 서버와 통신 실패: {}", e.getMessage());
            return false;
        }
    }
}
