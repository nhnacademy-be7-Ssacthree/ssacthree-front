package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingGetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//contextId 안 써주면 bean 찾을때 ? 오류남.
@FeignClient(name="gateway-service", url = "${admin-client.url}", contextId = "packagingClient")
public interface PackagingAdapter {

    @GetMapping("/packaging")
    ResponseEntity<List<PackagingGetResponse>> getAllPackaging();

}
