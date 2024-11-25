package com.nhnacademy.mini_dooray.ssacthree_front.member.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PaycoLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PointHistoryGetResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "memberClient")
public interface MemberAdapter {

    @PostMapping("/shop/members")
    ResponseEntity<MessageResponse> memberRegister(
        @RequestBody MemberRegisterRequest memberRegisterRequest);

    @DeleteMapping("/shop/members")
    ResponseEntity<MessageResponse> memberDelete(
        @RequestHeader("Authorization") String authorizationHeader);

    @PostMapping("/auth/login")
    ResponseEntity<MessageResponse> memberLogin(@RequestBody MemberLoginRequest memberLoginRequest);

    @PostMapping("/auth/payco-login")
    ResponseEntity<String> memberPaycoLogin(
        @RequestBody PaycoLoginRequest paycoLoginRequest);

    @PostMapping("/auth/payco-connection")
    ResponseEntity<String> memberPaycoConnection(@RequestBody PaycoLoginRequest paycoLoginRequest);


    @PostMapping("/auth/logout")
    ResponseEntity<MessageResponse> memberLogout();


    @GetMapping("/shop/members/my-page")
    ResponseEntity<MemberInfoResponse> memberInfo(
        @RequestHeader("Authorization") String authorizationHeader);

    @PutMapping("/shop/members/my-page")
    ResponseEntity<MessageResponse> memberInfoUpdate(
        @RequestHeader("Authorization") String authorizationHeader, @RequestBody
    MemberInfoUpdateRequest memberInfoUpdateRequest);


    @PostMapping("/shop/members/address")
    ResponseEntity<AddressResponse> addNewAddress(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestBody AddressRequest addressRequest);

    @GetMapping("/shop/members/address")
    ResponseEntity<List<AddressResponse>> getAddresses(
        @RequestHeader("Authorization") String authorizationHeader);

    @DeleteMapping("/shop/members/address/{id}")
    ResponseEntity<Void> deleteAddress(@RequestHeader("Authorization") String authorizationHeader,
        @PathVariable Long id);


    @GetMapping("/shop/members/point-histories")
    ResponseEntity<Page<PointHistoryGetResponse>> getPointHistories(@RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("sort") String sort,
        @RequestParam("direction") String direction);


}
