package com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto.PublisherGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="gateway-service", url = "${admin-client.url}", contextId = "publisherClient")
public interface PublisherAdapter {
    @GetMapping("/publishers")
    ResponseEntity<List<PublisherGetResponse>> getAllPublishers();

    @PostMapping("/publishers")
    ResponseEntity<MessageResponse> createPublisher(@RequestBody PublisherCreateRequest publisherCreateRequest);

    @PutMapping("/publishers")
    ResponseEntity<MessageResponse> updatePublisher(@RequestBody PublisherUpdateRequest publisherUpdateRequest);
}
