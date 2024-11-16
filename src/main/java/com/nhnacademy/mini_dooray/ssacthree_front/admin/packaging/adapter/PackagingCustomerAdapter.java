package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingGetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name="gateway-service", url = "${member.url}", contextId = "packagingCustomerClient")
public interface PackagingCustomerAdapter {
    @GetMapping("/shop/packaging")
    ResponseEntity<List<PackagingGetResponse>> getAllPackaging();
}
