package com.nhnacademy.mini_dooray.ssacthree_front.customer.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.customer.dto.CustomerCreateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "customerClient")
public interface CustomerAdapter {

    @PostMapping("/shop/customers")
    ResponseEntity<Long> createCustomer(@RequestBody CustomerCreateRequest customerCreateRequest);
}
