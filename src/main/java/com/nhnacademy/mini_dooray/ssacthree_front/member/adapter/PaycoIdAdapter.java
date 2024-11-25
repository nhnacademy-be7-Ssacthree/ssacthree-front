package com.nhnacademy.mini_dooray.ssacthree_front.member.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PaycoMemberResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payco-id-adapter", url = "${payco.idNoUrl}")
public interface PaycoIdAdapter {

    @PostMapping
    ResponseEntity<PaycoMemberResponse> getIdNo(@RequestHeader(name = "client_id") String clientId,
        @RequestHeader(name = "access_token") String accessToken);
}
