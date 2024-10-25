package com.nhnacademy.mini_dooray.ssacthree_front.member.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="memberSendClient", url = "http://localhost:8081/api")
public interface MemberAdapter {

    @PostMapping("/members")
    ResponseEntity<MessageResponse> memberRegister(@RequestBody MemberRegisterRequest memberRegisterRequest);

}
