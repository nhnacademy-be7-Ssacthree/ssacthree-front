package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.ExceptionParser;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberSleepToActiveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LoginFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LogoutIllegalAccessException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.MemberInfoUpdateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.MemberNotFoundException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.MemberRegisterFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.SleepMemberLoginFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.SleepMemberReleaseFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.CertNumberService;
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

/**
 * @author 김희망
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {


    private final MemberAdapter memberAdapter;
    private final CertNumberService certNumberService;
    private static final String SET_COOKIE = "Set-Cookie";
    private static final String ACCESS_TOKEN = "access-token";
    private static final String HEADER_BEARER = "Bearer ";
    private static final String MEMBER_NOT_FOUND = "회원 정보를 불러올 수 없습니다.";

    /**
     * 회원가입 기능을 수행하는 메소드
     *
     * @param request 회원가입 DTO 전달
     * @return : 응답 상태가 200번대 인 경우 MessageResponse 리턴, 아닐경우 Exception 발생
     */
    @Override
    public MessageResponse memberRegister(MemberRegisterRequest request) {

        try {
            ResponseEntity<MessageResponse> response = memberAdapter.memberRegister(request);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new MemberRegisterFailedException("회원가입에 실패하였습니다.");
        } catch (FeignException e) {
            throw new MemberRegisterFailedException(
                // e.contentUTF8 --> response body
                ExceptionParser.getErrorMessageFromFeignException(e.contentUTF8()));
        }

    }

    /**
     * 회원 로그인 기능을 수행하는 메소드
     *
     * @param requestBody         로그인 DTO 전달
     * @param httpServletResponse ServletResponse를 받아서 쿠키 전달
     * @return : 응답 상태가 200번대 인 경우 MessageResponse 리턴, 아닐경우 Exception 발생
     */
    @Override
    public MessageResponse memberLogin(MemberLoginRequest requestBody,
        HttpServletResponse httpServletResponse) {
        try {
            ResponseEntity<MessageResponse> response = memberAdapter.memberLogin(requestBody);
            if (isHaveCookie(httpServletResponse, response)) {
                return response.getBody();
            }

            throw new LoginFailedException("로그인에 실패하였습니다.");
        } catch (FeignException e) {
            log.debug(ExceptionParser.getErrorMessageFromFeignException(e.contentUTF8()));
            if (ExceptionParser.getErrorCodeFromFeignException(e.contentUTF8()).equals("423")) {
                throw new SleepMemberLoginFailedException("휴면 계정입니다.", requestBody.getLoginId());
            }
            throw new LoginFailedException("로그인에 실패하였습니다.");
        }
    }

    /**
     * 회원 로그아웃을 기능을 수행하는 메소드
     *
     * @param httpServletResponse ServletResponse를 전달하여 쿠키 제거
     * @return : 응답 상태가 200번대 인 경우 MessageResponse 리턴, 아닐경우 Exception 발생
     */
    @Override
    public MessageResponse memberLogout(HttpServletResponse httpServletResponse) {

        try {
            ResponseEntity<MessageResponse> response = memberAdapter.memberLogout();
            if (isHaveCookie(httpServletResponse, response)) {
                return response.getBody();
            }

            throw new LogoutIllegalAccessException("잘못된 접근입니다.");
        } catch (FeignException e) {
            throw new LogoutIllegalAccessException("잘못된 접근입니다.");
        }
    }

    /**
     * Response에 쿠키가 들어오는지 확인하는 메소드
     *
     * @param httpServletResponse ServletResponse를 전달하여 FeignClient가 가져온 쿠키 생성
     * @param response            FeignClient의 응답 결과
     * @return : 응답 상태가 200번대 인 경우 MessageResponse 리턴, 아닐경우 Exception 발생
     */
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

    /**
     * 회원 정보를 얻어오는 메소드
     *
     * @param request servletRequest를 전달받아 쿠키 추출에 이용
     * @return : 응답 상태가 200번대 인 경우 MessageResponse 리턴, 아닐경우 Exception 발생
     */
    @Override
    public MemberInfoResponse getMemberInfo(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(ACCESS_TOKEN)) {
                accessToken = cookie.getValue();
                break;
            }
        }
        try {
            ResponseEntity<MemberInfoResponse> response = memberAdapter.memberInfo(
                HEADER_BEARER + accessToken);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (FeignException e) {
            throw new MemberNotFoundException(MEMBER_NOT_FOUND);
        }

        throw new MemberNotFoundException(MEMBER_NOT_FOUND);
    }

    /**
     * 회원 정보 수정 기능을 수행하는 메소드
     *
     * @param requestBody 회원정보 update DTO
     * @param request     request로 쿠키 체크
     * @return : 응답 상태가 200번대 인 경우 MessageResponse 리턴, 아닐경우 Exception 발생
     */
    @Override
    public MessageResponse memberInfoUpdate(MemberInfoUpdateRequest requestBody,
        HttpServletRequest request) {
        
        String accessToken = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(ACCESS_TOKEN)) {
                accessToken = cookie.getValue();
            }
        }
        try {
            ResponseEntity<MessageResponse> response = memberAdapter.memberInfoUpdate(
                HEADER_BEARER + accessToken, requestBody);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (FeignException e) {
            throw new MemberInfoUpdateFailedException("회원 수정이 불가합니다.");
        }

        throw new MemberNotFoundException(MEMBER_NOT_FOUND);
    }

    /**
     * 회원 탈퇴를 처리하는 메소드
     *
     * @param request  ServletRequest를 전달 받아 쿠키 체크
     * @param response SerlvetResponse를 전달 받아 쿠키 제거
     * @return : 응답 상태가 200번대 인 경우 MessageResponse 리턴, 아닐경우 Exception 발생
     */
    @Override
    public MessageResponse memberWithdraw(HttpServletRequest request,
        HttpServletResponse response) {

        String accessToken = null;
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(ACCESS_TOKEN)) {
                accessToken = cookie.getValue();
            }
        }
        try {
            ResponseEntity<MessageResponse> feignResponse = memberAdapter.memberDelete(
                HEADER_BEARER + accessToken);
            if (feignResponse.getStatusCode().is2xxSuccessful()) {

                this.memberLogout(response);
                return feignResponse.getBody();
            }
        } catch (FeignException e) {
            throw new MemberNotFoundException("회원을 찾을 수 없습니다.");
        }
        throw new MemberNotFoundException("회원을 찾을 수 없습니다.");
    }

    /**
     * 휴면 회원인 멤버의 상태를 다시 Active로 변경하는 메소드
     *
     * @param memberSleepToActiveRequest 휴면 해제 DTO
     * @return MessageResponse
     */
    @Override
    public MessageResponse memberSleepToActive(
        MemberSleepToActiveRequest memberSleepToActiveRequest) {

        String certNumber = certNumberService.getCertNumber(
            memberSleepToActiveRequest.getMemberLoginId());

        if (certNumber == null) {
            throw new SleepMemberReleaseFailedException("잘못된 요청입니다.");

        }

        if (!certNumber.equals(memberSleepToActiveRequest.getCertNumber())) {
            certNumberService.delete(certNumber);
            throw new SleepMemberReleaseFailedException("인증번호가 다릅니다.");
        }
        try {
            ResponseEntity<MessageResponse> response = memberAdapter.memberActive(
                memberSleepToActiveRequest.getMemberLoginId());
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new SleepMemberReleaseFailedException("휴면 해제를 실패하였습니다.");
        } catch (FeignException e) {
            throw new SleepMemberReleaseFailedException("휴면 해제를 실패하였습니다.");
        } finally {
            certNumberService.delete(certNumber);
        }

    }

}
