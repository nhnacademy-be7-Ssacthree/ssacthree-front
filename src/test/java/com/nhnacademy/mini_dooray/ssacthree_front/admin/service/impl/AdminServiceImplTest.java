package com.nhnacademy.mini_dooray.ssacthree_front.admin.service.impl;

import static org.mockito.Mockito.times;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.adapter.AdminAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.AdminLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.CookieUtil;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LoginFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LogoutIllegalAccessException;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminServiceImpl;

    @Mock
    private AdminAdapter adminAdapter;

    @Mock
    private HttpServletResponse httpServletResponse;

    @Test
    void testLogin_Success() {
        // Given
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest("admin", "password");

        MessageResponse messageResponse = new MessageResponse("Success");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(messageResponse,
            HttpStatus.OK);

        Mockito.when(adminAdapter.adminLogin(adminLoginRequest)).thenReturn(responseEntity);

        // Mock the static method
        try (MockedStatic<CookieUtil> cookieUtilMockedStatic = Mockito.mockStatic(
            CookieUtil.class)) {

            // When
            MessageResponse result = adminServiceImpl.login(httpServletResponse, adminLoginRequest);

            // Then
            Assertions.assertEquals(messageResponse, result);
            cookieUtilMockedStatic.verify(
                () -> CookieUtil.addCookieFromFeignClient(httpServletResponse, responseEntity),
                times(1));
        }
    }

    @Test
    void testLogin_Failure_Non2xxStatus() {
        // Given
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest("admin", "wrongpassword");

        MessageResponse messageResponse = new MessageResponse("Unauthorized");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(messageResponse,
            HttpStatus.UNAUTHORIZED);

        Mockito.when(adminAdapter.adminLogin(adminLoginRequest)).thenReturn(responseEntity);

        // When & Then
        Assertions.assertThrows(LoginFailedException.class, () -> {
            adminServiceImpl.login(httpServletResponse, adminLoginRequest);
        });
    }

    @Test
    void testLogin_Failure_FeignException() {
        // Given
        AdminLoginRequest adminLoginRequest = new AdminLoginRequest("admin", "password");

        Mockito.when(adminAdapter.adminLogin(adminLoginRequest)).thenThrow(FeignException.class);

        // When & Then
        Assertions.assertThrows(LoginFailedException.class, () -> {
            adminServiceImpl.login(httpServletResponse, adminLoginRequest);
        });
    }

    @Test
    void testLogout_Success() {
        // Given
        MessageResponse messageResponse = new MessageResponse("Logged out");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", "cookie1=value1");
        headers.add("Set-Cookie", "cookie2=value2");

        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(messageResponse,
            headers, HttpStatus.OK);

        Mockito.when(adminAdapter.logout()).thenReturn(responseEntity);

        // When
        MessageResponse result = adminServiceImpl.logout(httpServletResponse);

        // Then
        Assertions.assertEquals(messageResponse, result);
        // Verify that headers were added to httpServletResponse
        Mockito.verify(httpServletResponse).addHeader("Set-Cookie", "cookie1=value1");
        Mockito.verify(httpServletResponse).addHeader("Set-Cookie", "cookie2=value2");
    }

    @Test
    void testLogout_Failure_Non2xxStatus() {
        // Given
        MessageResponse messageResponse = new MessageResponse("Error");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(messageResponse,
            HttpStatus.INTERNAL_SERVER_ERROR);

        Mockito.when(adminAdapter.logout()).thenReturn(responseEntity);

        // When & Then
        Assertions.assertThrows(LogoutIllegalAccessException.class, () -> {
            adminServiceImpl.logout(httpServletResponse);
        });
    }


    @Test
    void testLogout_Failure_NoCookiesInHeaders() {
        // Given
        MessageResponse messageResponse = new MessageResponse("Logged out");
        HttpHeaders headers = new HttpHeaders();  // No Set-Cookie headers
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(messageResponse,
            headers, HttpStatus.OK);

        Mockito.when(adminAdapter.logout()).thenReturn(responseEntity);

        // When & Then
        Assertions.assertThrows(AssertionError.class, () -> {
            adminServiceImpl.logout(httpServletResponse);
        });
    }
}
