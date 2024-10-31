package com.nhnacademy.mini_dooray.ssacthree_front.member.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="gateway-service", url = "${member.url}",contextId = "memberClient")
public interface MemberAdapter {

    @PostMapping("/shop/members")
    ResponseEntity<MessageResponse> memberRegister(@RequestBody MemberRegisterRequest memberRegisterRequest);

    @PostMapping("/auth/login")
    ResponseEntity<MessageResponse> memberLogin(@RequestBody MemberLoginRequest memberLoginRequest);
}
