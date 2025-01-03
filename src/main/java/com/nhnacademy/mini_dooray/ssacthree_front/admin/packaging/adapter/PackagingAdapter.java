package com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//contextId 안 써주면 bean 찾을때 ? 오류남.
@FeignClient(name="gateway-service", url = "${admin-client.url}", contextId = "packagingClient")
public interface PackagingAdapter {

    @GetMapping("/packaging")
    ResponseEntity<List<PackagingGetResponse>> getAllPackaging();

    @PostMapping("/packaging")
    ResponseEntity<MessageResponse> createPackaging(@RequestBody PackagingCreateRequest packagingCreateRequest);

    @PutMapping("/packaging/{packaging-id}")
    ResponseEntity<MessageResponse> updatePackaging(@PathVariable("packaging-id") Long packagingId, PackagingUpdateRequest packagingUpdateRequest);

    @DeleteMapping("/packaging/{packaging-id}")
    ResponseEntity<MessageResponse> deletePackaging(@PathVariable("packaging-id") Long packagingId);
}
