package com.nhnacademy.mini_dooray.ssacthree_front.address;

import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.AddAddressFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.AddressFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl.AddressServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    @Mock
    private MemberAdapter memberAdapter;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddNewAddressSuccess() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        AddressRequest addressRequest = new AddressRequest();
        AddressResponse addressResponse = new AddressResponse();
        ResponseEntity<AddressResponse> response = new ResponseEntity<>(addressResponse, HttpStatus.OK);

        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access-token", "test-token")});
        when(memberAdapter.addNewAddress("Bearer test-token", addressRequest)).thenReturn(response);

        AddressResponse result = addressService.addNewAddress(request, addressRequest);
        assertNotNull(result);
        assertEquals(addressResponse, result);
    }

    @Test
    void testAddNewAddressFailure() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        AddressRequest addressRequest = new AddressRequest();

        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access-token", "test-token")});
        when(memberAdapter.addNewAddress("Bearer test-token", addressRequest))
            .thenThrow(new AddAddressFailedException("주소 입력에 실패하였습니다."));

        assertThrows(AddAddressFailedException.class, () -> addressService.addNewAddress(request, addressRequest));
    }

    @Test
    void testGetAddressesSuccess() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        List<AddressResponse> addressList = List.of(new AddressResponse());
        ResponseEntity<List<AddressResponse>> response = new ResponseEntity<>(addressList, HttpStatus.OK);

        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access-token", "test-token")});
        when(memberAdapter.getAddresses("Bearer test-token")).thenReturn(response);

        List<AddressResponse> result = addressService.getAddresses(request);
        assertNotNull(result);
        assertEquals(addressList, result);
    }

    @Test
    void testGetAddressesFailure() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access-token", "test-token")});
        when(memberAdapter.getAddresses("Bearer test-token"))
            .thenThrow(new AddressFailedException("주소 응답에 실패하였습니다."));

        assertThrows(AddressFailedException.class, () -> addressService.getAddresses(request));
    }

    @Test
    void testDeleteAddressSuccess() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        ResponseEntity<Void> response = new ResponseEntity<>(HttpStatus.OK);

        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access-token", "test-token")});
        when(memberAdapter.deleteAddress("Bearer test-token", 1L)).thenReturn(response);

        assertDoesNotThrow(() -> addressService.deleteAddress(1L, request));
    }

    @Test
    void testDeleteAddressFailure() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access-token", "test-token")});
        when(memberAdapter.deleteAddress("Bearer test-token", 1L))
            .thenThrow(new AddressFailedException("주소 삭제에 실패하였습니다."));

        assertThrows(AddressFailedException.class, () -> addressService.deleteAddress(1L, request));
    }

    @Test
    void testGetAccessToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access-token", "test-token")});

        String token = addressService.getAccessToken(request);
        assertEquals("test-token", token);
    }

    @Test
    void testGetAccessTokenNoCookie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("other-cookie", "value")});

        String token = addressService.getAccessToken(request);
        assertNull(token);
    }
}
