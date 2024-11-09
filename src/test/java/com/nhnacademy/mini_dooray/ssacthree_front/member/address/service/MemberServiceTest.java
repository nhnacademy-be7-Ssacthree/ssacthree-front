package com.nhnacademy.mini_dooray.ssacthree_front.member.address.service;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.*;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl.MemberServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MemberServiceTest {

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberAdapter memberAdapter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private static final String ACCESS_TOKEN = "sampleAccessToken";
    private static final String REFRESH_TOKEN = "sampleRefreshToken";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testMemberRegister_success() {
        MemberRegisterRequest request = new MemberRegisterRequest(/* 필요한 필드 초기화 */);
        MessageResponse responseMessage = new MessageResponse("회원가입 성공");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.OK);

        when(memberAdapter.memberRegister(request)).thenReturn(responseEntity);

        MessageResponse result = memberService.memberRegister(request);
        assertEquals("회원가입 성공", result.getMessage());
    }

    @Test
    void testMemberLogin_success() {
        MemberLoginRequest loginRequest = new MemberLoginRequest(/* 필요한 필드 초기화 */);
        MessageResponse responseMessage = new MessageResponse("로그인 성공");
        HttpHeaders headers = new HttpHeaders();

        // 두 개의 쿠키 추가
        headers.add(HttpHeaders.SET_COOKIE, "access-token=" + ACCESS_TOKEN);
        headers.add(HttpHeaders.SET_COOKIE, "refresh-token=" + REFRESH_TOKEN);

        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);

        when(memberAdapter.memberLogin(loginRequest)).thenReturn(responseEntity);

        MessageResponse result = memberService.memberLogin(loginRequest, response);

        assertEquals("로그인 성공", result.getMessage());
        verify(response, times(1)).addHeader(HttpHeaders.SET_COOKIE, "access-token=" + ACCESS_TOKEN);
        verify(response, times(1)).addHeader(HttpHeaders.SET_COOKIE, "refresh-token=" + REFRESH_TOKEN); // refresh-token 검증
    }

    @Test
    void testMemberLogout_success() {
        MessageResponse responseMessage = new MessageResponse("로그아웃 성공");
        HttpHeaders headers = new HttpHeaders();

        // 두 개의 쿠키 추가
        headers.add(HttpHeaders.SET_COOKIE, "access-token=; Max-Age=0");
        headers.add(HttpHeaders.SET_COOKIE, "refresh-token=; Max-Age=0");

        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);

        when(memberAdapter.memberLogout()).thenReturn(responseEntity);

        MessageResponse result = memberService.memberLogout(response);

        assertEquals("로그아웃 성공", result.getMessage());
        verify(response, times(1)).addHeader(HttpHeaders.SET_COOKIE, "access-token=; Max-Age=0");
        verify(response, times(1)).addHeader(HttpHeaders.SET_COOKIE, "refresh-token=; Max-Age=0"); // refresh-token 검증
    }

    @Test
    void testGetMemberInfo_success() {
        Cookie accessTokenCookie = new Cookie("access-token", ACCESS_TOKEN);
        when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});
        MemberInfoResponse infoResponse = new MemberInfoResponse(/* 필요한 필드 초기화 */);
        ResponseEntity<MemberInfoResponse> responseEntity = new ResponseEntity<>(infoResponse, HttpStatus.OK);

        when(memberAdapter.memberInfo("Bearer " + ACCESS_TOKEN)).thenReturn(responseEntity);

        MemberInfoResponse result = memberService.getMemberInfo(request);
        assertEquals(infoResponse, result);
    }

    @Test
    void testMemberInfoUpdate_success() {
        MemberInfoUpdateRequest updateRequest = new MemberInfoUpdateRequest(/* 필요한 필드 초기화 */);
        Cookie accessTokenCookie = new Cookie("access-token", ACCESS_TOKEN);
        when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});
        MessageResponse responseMessage = new MessageResponse("회원정보 수정 성공");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.OK);

        when(memberAdapter.memberInfoUpdate("Bearer " + ACCESS_TOKEN, updateRequest)).thenReturn(responseEntity);

        MessageResponse result = memberService.memberInfoUpdate(updateRequest, request);
        assertEquals("회원정보 수정 성공", result.getMessage());
    }

    @Test
    void testMemberWithdraw_success() {
        Cookie accessTokenCookie = new Cookie("access-token", ACCESS_TOKEN);
        when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});
        MessageResponse responseMessage = new MessageResponse("회원 탈퇴 성공");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(responseMessage, HttpStatus.OK);

        when(memberAdapter.memberDelete("Bearer " + ACCESS_TOKEN)).thenReturn(responseEntity);

        MessageResponse result = memberService.memberWithdraw(request);
        assertEquals("회원 탈퇴 성공", result.getMessage());
    }


}