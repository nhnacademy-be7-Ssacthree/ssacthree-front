package com.nhnacademy.mini_dooray.ssacthree_front.member.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl.MemberServiceImpl;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberAdapter memberAdapter;

    @Mock
    private CertNumberService certNumberService;

    @InjectMocks
    private MemberServiceImpl memberService;

    private static final String SET_COOKIE = "Set-Cookie";
    private static final String ACCESS_TOKEN = "access-token";
    private static final String HEADER_BEARER = "Bearer ";
    private static final String MEMBER_NOT_FOUND = "회원 정보를 불러올 수 없습니다.";


    @Test
    void testMemberRegister_Success() {
        // Arrange
        MemberRegisterRequest request = new MemberRegisterRequest();
        // Set properties of request if needed

        MessageResponse expectedResponse = new MessageResponse("회원가입 성공");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(expectedResponse,
            HttpStatus.CREATED);

        when(memberAdapter.memberRegister(any(MemberRegisterRequest.class))).thenReturn(
            responseEntity);

        // Act
        MessageResponse actualResponse = memberService.memberRegister(request);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testMemberRegister_Failure() {
        // Arrange
        MemberRegisterRequest request = new MemberRegisterRequest();
        // Set properties of request if needed

        FeignException feignException = mock(FeignException.class);
        when(feignException.contentUTF8()).thenReturn("회원가입에 실패하였습니다.");

        when(memberAdapter.memberRegister(any(MemberRegisterRequest.class))).thenThrow(
            feignException);

        // Act & Assert
        MemberRegisterFailedException exception = assertThrows(MemberRegisterFailedException.class,
            () -> {
                memberService.memberRegister(request);
            });

        assertEquals("회원가입에 실패하였습니다.", exception.getMessage());
    }

    @Test
    void testMemberLogin_Success() {
        // Arrange
        MemberLoginRequest requestBody = new MemberLoginRequest("test", "test");
        // Set properties of requestBody if needed

        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

        MessageResponse expectedResponse = new MessageResponse("로그인 성공");
        HttpHeaders headers = new HttpHeaders();
        headers.add(SET_COOKIE, "cookie1");
        headers.add(SET_COOKIE, "cookie2");

        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(expectedResponse,
            headers, HttpStatus.OK);

        when(memberAdapter.memberLogin(any(MemberLoginRequest.class))).thenReturn(responseEntity);

        // Act
        MessageResponse actualResponse = memberService.memberLogin(requestBody,
            httpServletResponse);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(httpServletResponse, times(2)).addHeader(eq(SET_COOKIE), anyString());
    }


    @Test
    void testMemberLogout_Success() {
        // Arrange
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

        MessageResponse expectedResponse = new MessageResponse("로그아웃 성공");
        HttpHeaders headers = new HttpHeaders();
        headers.add(SET_COOKIE, "cookie1");
        headers.add(SET_COOKIE, "cookie2");

        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(expectedResponse,
            headers, HttpStatus.OK);

        when(memberAdapter.memberLogout()).thenReturn(responseEntity);

        // Act
        MessageResponse actualResponse = memberService.memberLogout(httpServletResponse);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(httpServletResponse, times(2)).addHeader(eq(SET_COOKIE), anyString());
    }

    @Test
    void testMemberLogout_Failure() {
        // Arrange
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

        FeignException feignException = mock(FeignException.class);
        when(memberAdapter.memberLogout()).thenThrow(feignException);

        // Act & Assert
        LogoutIllegalAccessException exception = assertThrows(LogoutIllegalAccessException.class,
            () -> {
                memberService.memberLogout(httpServletResponse);
            });

        assertEquals("잘못된 접근입니다.", exception.getMessage());
    }

    @Test
    void testGetMemberInfo_Success() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[]{new Cookie(ACCESS_TOKEN, "testAccessToken")};
        when(request.getCookies()).thenReturn(cookies);

        MemberInfoResponse expectedResponse = new MemberInfoResponse();
        // Set properties of expectedResponse if needed

        ResponseEntity<MemberInfoResponse> responseEntity = new ResponseEntity<>(expectedResponse,
            HttpStatus.OK);

        when(memberAdapter.memberInfo(eq(HEADER_BEARER + "testAccessToken"))).thenReturn(
            responseEntity);

        // Act
        MemberInfoResponse actualResponse = memberService.getMemberInfo(request);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testGetMemberInfo_Failure() {
        // Arrange
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[]{new Cookie(ACCESS_TOKEN, "testAccessToken")};
        when(request.getCookies()).thenReturn(cookies);

        FeignException feignException = mock(FeignException.class);
        when(memberAdapter.memberInfo(eq(HEADER_BEARER + "testAccessToken"))).thenThrow(
            feignException);

        // Act & Assert
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () -> {
            memberService.getMemberInfo(request);
        });

        assertEquals(MEMBER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void testMemberInfoUpdate_Success() {
        // Arrange
        MemberInfoUpdateRequest requestBody = new MemberInfoUpdateRequest();
        // Set properties of requestBody if needed

        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[]{new Cookie(ACCESS_TOKEN, "testAccessToken")};
        when(request.getCookies()).thenReturn(cookies);

        MessageResponse expectedResponse = new MessageResponse("회원정보 수정 성공");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(expectedResponse,
            HttpStatus.OK);

        when(memberAdapter.memberInfoUpdate(eq(HEADER_BEARER + "testAccessToken"),
            any(MemberInfoUpdateRequest.class)))
            .thenReturn(responseEntity);

        // Act
        MessageResponse actualResponse = memberService.memberInfoUpdate(requestBody, request);

        // Assert
        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testMemberInfoUpdate_Failure() {
        // Arrange
        MemberInfoUpdateRequest requestBody = new MemberInfoUpdateRequest();
        // Set properties of requestBody if needed

        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[]{new Cookie(ACCESS_TOKEN, "testAccessToken")};
        when(request.getCookies()).thenReturn(cookies);

        FeignException feignException = mock(FeignException.class);
        when(memberAdapter.memberInfoUpdate(eq(HEADER_BEARER + "testAccessToken"),
            any(MemberInfoUpdateRequest.class)))
            .thenThrow(feignException);

        // Act & Assert
        MemberInfoUpdateFailedException exception = assertThrows(
            MemberInfoUpdateFailedException.class, () -> {
                memberService.memberInfoUpdate(requestBody, request);
            });

        assertEquals("회원 수정이 불가합니다.", exception.getMessage());
    }

    @Test
    void testMemberWithdraw_success() {
        Cookie accessTokenCookie = new Cookie("access-token", ACCESS_TOKEN);

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // `getCookies`가 null이 아닌 쿠키 배열을 반환하도록 설정
        when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});

        // 회원 탈퇴와 로그아웃 성공 메시지 및 응답 설정
        MessageResponse withdrawResponseMessage = new MessageResponse("회원 탈퇴 성공");
        MessageResponse logoutResponseMessage = new MessageResponse("로그아웃 성공");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, "access-token=; Max-Age=0");
        headers.add(HttpHeaders.SET_COOKIE, "refresh-token=; Max-Age=0");

        // 회원 탈퇴와 로그아웃 응답 설정
        ResponseEntity<MessageResponse> withdrawResponseEntity = new ResponseEntity<>(
            withdrawResponseMessage, HttpStatus.OK);
        ResponseEntity<MessageResponse> logoutResponseEntity = new ResponseEntity<>(
            logoutResponseMessage, headers, HttpStatus.OK);

        when(memberAdapter.memberDelete("Bearer " + ACCESS_TOKEN)).thenReturn(
            withdrawResponseEntity);
        when(memberAdapter.memberLogout()).thenReturn(logoutResponseEntity);

        // 실제 테스트 수행
        MessageResponse result = memberService.memberWithdraw(request, response);

        // 결과 확인
        assertEquals("회원 탈퇴 성공", result.getMessage());
    }

    @Test
    void testMemberLogin_Failure_InvalidCredentials() {
        // Arrange
        MemberLoginRequest requestBody = new MemberLoginRequest("test", "test");

        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

        FeignException feignException = mock(FeignException.class);
        when(feignException.contentUTF8()).thenReturn(
            "{\"errorCode\":\"401\",\"errorMessage\":\"로그인에 실패하였습니다.\"}");

        when(memberAdapter.memberLogin(any(MemberLoginRequest.class))).thenThrow(feignException);

        // Act & Assert
        LoginFailedException exception = assertThrows(LoginFailedException.class, () -> {
            memberService.memberLogin(requestBody, httpServletResponse);
        });

        assertEquals("로그인에 실패하였습니다.", exception.getMessage());
    }

    @Test
    void testMemberLogin_Failure_SleepMember() {
        // Arrange
        MemberLoginRequest requestBody = new MemberLoginRequest("test", "test");

        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

        FeignException feignException = mock(FeignException.class);
        when(feignException.contentUTF8()).thenReturn(
            "{\"errorCode\":\"423\",\"errorMessage\":\"휴면 계정입니다.\"}");
        when(ExceptionParser.getErrorCodeFromFeignException(
            feignException.contentUTF8())).thenReturn("423");
        when(memberAdapter.memberLogin(any(MemberLoginRequest.class))).thenThrow(feignException);

        // Act & Assert
        SleepMemberLoginFailedException exception = assertThrows(
            SleepMemberLoginFailedException.class, () -> {
                memberService.memberLogin(requestBody, httpServletResponse);
            });

        assertEquals("휴면 계정입니다.", exception.getMessage());

    }


    @Test
    void testMemberSleepToActive_Success() {
        // Arrange
        MemberSleepToActiveRequest sleepToActiveRequest = new MemberSleepToActiveRequest();
        sleepToActiveRequest.setMemberLoginId("testUser");
        sleepToActiveRequest.setCertNumber("123456");

        when(certNumberService.getCertNumber("testUser")).thenReturn("123456");

        MessageResponse expectedResponse = new MessageResponse("휴면 해제 성공");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(expectedResponse,
            HttpStatus.OK);

        when(memberAdapter.memberActive("testUser")).thenReturn(responseEntity);

        // Act
        MessageResponse actualResponse = memberService.memberSleepToActive(sleepToActiveRequest);

        // Assert
        assertEquals(expectedResponse, actualResponse);
        verify(certNumberService).delete("123456");
    }

    @Test
    void testMemberSleepToActive_Failure_InvalidCertNumber() {
        // Arrange
        MemberSleepToActiveRequest sleepToActiveRequest = new MemberSleepToActiveRequest();
        sleepToActiveRequest.setMemberLoginId("testUser");
        sleepToActiveRequest.setCertNumber("wrongCertNumber");

        when(certNumberService.getCertNumber("testUser")).thenReturn("123456");

        // Act & Assert
        SleepMemberReleaseFailedException exception = assertThrows(
            SleepMemberReleaseFailedException.class, () -> {
                memberService.memberSleepToActive(sleepToActiveRequest);
            });

        assertEquals("인증번호가 다릅니다.", exception.getMessage());
        verify(certNumberService).delete("123456");
    }

    @Test
    void testMemberSleepToActive_Failure_NoCertNumber() {
        // Arrange
        MemberSleepToActiveRequest sleepToActiveRequest = new MemberSleepToActiveRequest();
        sleepToActiveRequest.setMemberLoginId("testUser");
        sleepToActiveRequest.setCertNumber("123456");

        when(certNumberService.getCertNumber("testUser")).thenReturn(null);

        // Act & Assert
        SleepMemberReleaseFailedException exception = assertThrows(
            SleepMemberReleaseFailedException.class, () -> {
                memberService.memberSleepToActive(sleepToActiveRequest);
            });

        assertEquals("잘못된 요청입니다.", exception.getMessage());
    }

    @Test
    void testMemberSleepToActive_Failure_FeignException() {
        // Arrange
        MemberSleepToActiveRequest sleepToActiveRequest = new MemberSleepToActiveRequest();
        sleepToActiveRequest.setMemberLoginId("testUser");
        sleepToActiveRequest.setCertNumber("123456");

        when(certNumberService.getCertNumber("testUser")).thenReturn("123456");

        FeignException feignException = mock(FeignException.class);
        when(memberAdapter.memberActive("testUser")).thenThrow(feignException);

        // Act & Assert
        SleepMemberReleaseFailedException exception = assertThrows(
            SleepMemberReleaseFailedException.class, () -> {
                memberService.memberSleepToActive(sleepToActiveRequest);
            });

        assertEquals("휴면 해제를 실패하였습니다.", exception.getMessage());
        verify(certNumberService).delete("123456");
    }
}