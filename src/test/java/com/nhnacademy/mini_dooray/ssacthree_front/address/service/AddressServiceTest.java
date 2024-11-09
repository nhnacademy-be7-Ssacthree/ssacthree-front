package com.nhnacademy.mini_dooray.ssacthree_front.address.service;

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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @InjectMocks
    private AddressServiceImpl addressService;

    @Mock
    private MemberAdapter memberAdapter;

    @Mock
    private HttpServletRequest request;

    private static final String ACCESS_TOKEN = "test-access-token";

    @BeforeEach
    void setUp() {
        Cookie cookie = new Cookie("access-token", ACCESS_TOKEN);
        when(request.getCookies()).thenReturn(new Cookie[]{cookie});
    }

    @Test
    void testAddNewAddress_Success() {
        AddressRequest addressRequest = new AddressRequest("Home", "123 Street", "Main Road", "12345");
        AddressResponse addressResponse = new AddressResponse(1,"Home", "123 Street", "Main Road", "12345");

        when(memberAdapter.addNewAddress("Bearer " + ACCESS_TOKEN, addressRequest))
            .thenReturn(new ResponseEntity<>(addressResponse, HttpStatus.CREATED));

        AddressResponse result = addressService.addNewAddress(request, addressRequest);

        assertNotNull(result);
        assertEquals("Home", result.getAddressAlias());
        verify(memberAdapter, times(1)).addNewAddress("Bearer " + ACCESS_TOKEN, addressRequest);
    }

    @Test
    void testAddNewAddress_Failure() {
        AddressRequest addressRequest = new AddressRequest("Home", "123 Street", "Main Road", "12345");

        when(memberAdapter.addNewAddress("Bearer " + ACCESS_TOKEN, addressRequest))
            .thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

        assertThrows(AddAddressFailedException.class, () -> addressService.addNewAddress(request, addressRequest));
    }

    @Test
    void testGetAddresses_Success() {
        List<AddressResponse> addressResponses = List.of(
            new AddressResponse(1,"Home", "123 Street", "Main Road", "12345")
        );

        when(memberAdapter.getAddresses("Bearer " + ACCESS_TOKEN))
            .thenReturn(new ResponseEntity<>(addressResponses, HttpStatus.OK));

        List<AddressResponse> result = addressService.getAddresses(request);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Home", result.getFirst().getAddressAlias());
        verify(memberAdapter, times(1)).getAddresses("Bearer " + ACCESS_TOKEN);
    }

    @Test
    void testGetAddresses_Failure() {
        when(memberAdapter.getAddresses("Bearer " + ACCESS_TOKEN))
            .thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

        assertThrows(AddressFailedException.class, () -> addressService.getAddresses(request));
    }

    @Test
    void testDeleteAddress_Success() {
        long addressId = 1L;

        when(memberAdapter.deleteAddress("Bearer " + ACCESS_TOKEN, addressId))
            .thenReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));

        assertDoesNotThrow(() -> addressService.deleteAddress(addressId, request));
        verify(memberAdapter, times(1)).deleteAddress("Bearer " + ACCESS_TOKEN, addressId);
    }

    @Test
    void testDeleteAddress_Failure() {
        long addressId = 1L;

        when(memberAdapter.deleteAddress("Bearer " + ACCESS_TOKEN, addressId))
            .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        assertThrows(AddressFailedException.class, () -> addressService.deleteAddress(addressId, request));
    }

    @Test
    void testGetAccessToken_Success() {
        String token = addressService.getAccessToken(request);
        assertEquals(ACCESS_TOKEN, token);
    }

    @Test
    void testGetAccessToken_NoToken() {
        when(request.getCookies()).thenReturn(new Cookie[]{});
        String token = addressService.getAccessToken(request);
        assertNull(token);
    }

    @Test
    void testAddNewAddress_HttpClientErrorException() {
        AddressRequest addressRequest = new AddressRequest("Home", "123 Street", "Main Road", "12345");

        when(memberAdapter.addNewAddress("Bearer " + ACCESS_TOKEN, addressRequest))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(AddAddressFailedException.class, () -> addressService.addNewAddress(request, addressRequest));
    }

    @Test
    void testAddNewAddress_HttpServerErrorException() {
        AddressRequest addressRequest = new AddressRequest("Home", "123 Street", "Main Road", "12345");

        when(memberAdapter.addNewAddress("Bearer " + ACCESS_TOKEN, addressRequest))
            .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(AddAddressFailedException.class, () -> addressService.addNewAddress(request, addressRequest));
    }

    @Test
    void testGetAddresses_HttpClientErrorException() {
        when(memberAdapter.getAddresses("Bearer " + ACCESS_TOKEN))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(AddressFailedException.class, () -> addressService.getAddresses(request));
    }

    @Test
    void testGetAddresses_HttpServerErrorException() {
        when(memberAdapter.getAddresses("Bearer " + ACCESS_TOKEN))
            .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(AddressFailedException.class, () -> addressService.getAddresses(request));
    }

    @Test
    void testDeleteAddress_HttpClientErrorException() {
        long addressId = 1L;

        when(memberAdapter.deleteAddress("Bearer " + ACCESS_TOKEN, addressId))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        assertThrows(AddressFailedException.class, () -> addressService.deleteAddress(addressId, request));
    }

    @Test
    void testDeleteAddress_HttpServerErrorException() {
        long addressId = 1L;

        when(memberAdapter.deleteAddress("Bearer " + ACCESS_TOKEN, addressId))
            .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        assertThrows(AddressFailedException.class, () -> addressService.deleteAddress(addressId, request));
    }
}
