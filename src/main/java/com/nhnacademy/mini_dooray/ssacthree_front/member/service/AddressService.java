package com.nhnacademy.mini_dooray.ssacthree_front.member.service;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressRequest;

public interface AddressService {
    MessageResponse addNewAddress(AddressRequest addressRequest);

}
