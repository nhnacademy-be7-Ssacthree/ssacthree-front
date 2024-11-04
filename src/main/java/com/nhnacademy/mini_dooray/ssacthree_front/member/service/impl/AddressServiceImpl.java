package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.AddAddressFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.MemberRegisterFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final MemberAdapter memberAdapter;


    @Override
    public MessageResponse addNewAddress(AddressRequest addressRequest) {
        ResponseEntity<MessageResponse> response = memberAdapter.addNewAddress(addressRequest);

        try {
            if(response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new AddAddressFailedException("주소 입력에 실패하였습니다.");
        }
        catch (HttpClientErrorException | HttpServerErrorException e ) {
            throw new AddAddressFailedException("주소 입력에 실패하였습니다.");
        }
    }
}
