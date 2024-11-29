package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.PaycoIdAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.PaycoTokenAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PaycoGetTokenResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PaycoLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PaycoMemberResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LoginFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.PaycoConnectionFailedException;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class PaycoServiceTest {

    @InjectMocks
    private PaycoServiceImpl paycoService;

    @Mock
    private PaycoTokenAdapter paycoTokenAdapter;

    @Mock
    private PaycoIdAdapter paycoIdAdapter;

    @Mock
    private MemberAdapter memberAdapter;

    @BeforeEach
    void setUp() throws Exception {
        setPrivateField("authorizationCodeUrl", "https://authorization-code-url");
        setPrivateField("clientId", "test-client-id");
        setPrivateField("responseType", "code");
        setPrivateField("redirectUrl", "https://redirect-url");
        setPrivateField("redirectUrlForConnection", "https://redirect-url-for-connection");
        setPrivateField("serviceProviderCode", "test-service-provider-code");
        setPrivateField("userLocale", "ko_KR");
        setPrivateField("grantType", "authorization_code");
        setPrivateField("clientSecret", "test-client-secret");
    }

    private void setPrivateField(String fieldName, String value) throws Exception {
        Field field = PaycoServiceImpl.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(paycoService, value);
    }

    @Test
    void testGetAuthorizationCodeUrl() {
        String expectedUrl = "redirect:https://authorization-code-url?client_id=test-client-id&response_type=code&redirect_uri=https://redirect-url&serviceProviderCode=test-service-provider-code&userLocale=ko_KR";
        String actualUrl = paycoService.getAuthorizationCodeUrl();

        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void testGetAuthorizationCodeForConnection() {
        String expectedUrl = "redirect:https://authorization-code-url?client_id=test-client-id&response_type=code&redirect_uri=https://redirect-url-for-connection&serviceProviderCode=test-service-provider-code&userLocale=ko_KR";
        String actualUrl = paycoService.getAuthorizationCodeForConnection();

        assertEquals(expectedUrl, actualUrl);
    }

    @Test
    void testGetAccessToken_Success() {
        String code = "test-code";
        PaycoGetTokenResponse tokenResponse = new PaycoGetTokenResponse(
            "access-token",
            "access-token-secret",
            "refresh-token",
            "Bearer",
            "3600",
            null
        );

        when(paycoTokenAdapter.getToken(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(ResponseEntity.ok(tokenResponse));

        String accessToken = paycoService.getAccessToken(code);

        assertNotNull(accessToken);
        assertEquals("access-token", accessToken);
    }

    @Test
    void testGetAccessToken_Failure() {
        String code = "test-code";

        when(paycoTokenAdapter.getToken(anyString(), anyString(), anyString(), anyString()))
            .thenThrow(FeignException.class);

        String accessToken = paycoService.getAccessToken(code);

        assertNull(accessToken);
    }

    @Test
    void testGetPaycoIdNo_Success() {
        // Given
        String accessToken = "access-token";

        // Mock PaycoMemberResponse 구조 설정
        PaycoMemberResponse.Member member = new PaycoMemberResponse.Member("test-idNo");
        PaycoMemberResponse.Data data = new PaycoMemberResponse.Data(member);
        PaycoMemberResponse.Header header = new PaycoMemberResponse.Header(true, 0, "Success");
        PaycoMemberResponse memberResponse = new PaycoMemberResponse(header, data);

        // Mocking PaycoIdAdapter behavior
        when(paycoIdAdapter.getIdNo(anyString(), eq(accessToken)))
            .thenReturn(ResponseEntity.ok(memberResponse));

        // When
        String idNo = paycoService.getPaycoIdNo(accessToken);

        // Then
        assertNotNull(idNo);
        assertEquals("test-idNo", idNo);
    }

    @Test
    void testGetPaycoIdNo_Failure() {
        String accessToken = "access-token";

        when(paycoIdAdapter.getIdNo(anyString(), anyString()))
            .thenThrow(FeignException.class);

        String idNo = paycoService.getPaycoIdNo(accessToken);

        assertNull(idNo);
    }

    @Test
    void paycoLogin_success() {
        // Given
        String paycoIdNo = "testPaycoIdNo";
        String token = "mockToken";
        HttpServletResponse response = mock(HttpServletResponse.class);

        // Mock SET_COOKIE 헤더 포함
        HttpHeaders headers = new HttpHeaders();
        headers.put(SET_COOKIE, List.of(
            "access-token=mockAccessToken; Path=/; HttpOnly",
            "refresh-token=mockRefreshToken; Path=/; HttpOnly"
        ));
        ResponseEntity<String> mockResponse = new ResponseEntity<>(token, headers, HttpStatus.OK);

        when(memberAdapter.memberPaycoLogin(any(PaycoLoginRequest.class))).thenReturn(mockResponse);

        // When
        String result = paycoService.paycoLogin(paycoIdNo, response);

        // Then
        assertEquals(token, result);
        verify(memberAdapter, times(1)).memberPaycoLogin(any(PaycoLoginRequest.class));
        verify(response, atLeastOnce()).addHeader(eq(SET_COOKIE), anyString());
    }

    @Test
    void testPaycoLogin_Failure_Non2xx() {
        String paycoIdNo = "test-idNo";
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Failure",
            HttpStatus.BAD_REQUEST);

        when(memberAdapter.memberPaycoLogin(any(PaycoLoginRequest.class)))
            .thenReturn(responseEntity);

        assertThrows(LoginFailedException.class, () -> {
            paycoService.paycoLogin(paycoIdNo, httpServletResponse);
        });
    }

    @Test
    void testPaycoLogin_Failure_Exception() {
        String paycoIdNo = "test-idNo";
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);

        when(memberAdapter.memberPaycoLogin(any(PaycoLoginRequest.class)))
            .thenThrow(FeignException.class);

        assertThrows(LoginFailedException.class, () -> {
            paycoService.paycoLogin(paycoIdNo, httpServletResponse);
        });
    }

    @Test
    void testPaycoConnect_Success() {
        String paycoIdNo = "test-idNo";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Connected", HttpStatus.OK);

        when(memberAdapter.memberPaycoConnection(any(PaycoLoginRequest.class)))
            .thenReturn(responseEntity);

        String result = paycoService.paycoConnect(paycoIdNo);

        assertNotNull(result);
        assertEquals("Connected", result);
        verify(memberAdapter, times(1)).memberPaycoConnection(any(PaycoLoginRequest.class));
    }

    @Test
    void testPaycoConnect_Failure_Non2xx() {
        String paycoIdNo = "test-idNo";
        ResponseEntity<String> responseEntity = new ResponseEntity<>("Failed",
            HttpStatus.BAD_REQUEST);

        when(memberAdapter.memberPaycoConnection(any(PaycoLoginRequest.class)))
            .thenReturn(responseEntity);

        assertThrows(PaycoConnectionFailedException.class, () -> {
            paycoService.paycoConnect(paycoIdNo);
        });
    }

    @Test
    void testPaycoConnect_Failure_Exception() {
        String paycoIdNo = "test-idNo";

        FeignException feignException = mock(FeignException.class);
        when(feignException.contentUTF8()).thenReturn("Error Message");

        when(memberAdapter.memberPaycoConnection(any(PaycoLoginRequest.class)))
            .thenThrow(feignException);

        assertThrows(PaycoConnectionFailedException.class, () -> {
            paycoService.paycoConnect(paycoIdNo);
        });
    }
}
