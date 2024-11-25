package com.nhnacademy.mini_dooray.ssacthree_front.member.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PaycoGetTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "payco-service", url = "${payco.token_url}")
public interface PaycoTokenAdapter {


    @GetMapping
    ResponseEntity<PaycoGetTokenResponse> getToken(@RequestParam("client_id") String clientId,
        @RequestParam("grant_type") String grantType,
        @RequestParam("code") String code,
        @RequestParam("client_secret") String clientSecret);


}
