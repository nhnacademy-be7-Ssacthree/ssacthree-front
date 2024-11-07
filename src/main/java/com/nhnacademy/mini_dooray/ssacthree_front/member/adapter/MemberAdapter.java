package com.nhnacademy.mini_dooray.ssacthree_front.member.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="gateway-service", url = "${member.url}",contextId = "memberClient")
public interface MemberAdapter {

    @PostMapping("/shop/members")
    ResponseEntity<MessageResponse> memberRegister(@RequestBody MemberRegisterRequest memberRegisterRequest);

    @PostMapping("/auth/login")
    ResponseEntity<MessageResponse> memberLogin(@RequestBody MemberLoginRequest memberLoginRequest);

    @PostMapping("/auth/logout")
    ResponseEntity<MessageResponse> memberLogout();

    @GetMapping("/shop/members/my-page")
    ResponseEntity<MemberInfoResponse> memberInfo(@RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("/shop/members/address")
    ResponseEntity<AddressResponse> addNewAddress(@RequestHeader("Authorization") String authorizationHeader, @RequestBody AddressRequest addressRequest);

    @GetMapping("/shop/members/address")
    ResponseEntity<List<AddressResponse>> getAddresses(@RequestHeader("Authorization") String authorizationHeader);

    @DeleteMapping("/shop/members/address/{id}")
    ResponseEntity<Void> deleteAddress(@RequestHeader("Authorization") String authorizationHeader,
        @PathVariable long id);

}
