package com.nhnacademy.mini_dooray.ssacthree_front.member.service;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface AddressService {
    AddressResponse addNewAddress(HttpServletRequest request, AddressRequest addressRequest);

    List<AddressResponse> getAddresses(HttpServletRequest request);

    void deleteAddress(long addressId, HttpServletRequest request);

    String getAccessToken(HttpServletRequest request);
}
