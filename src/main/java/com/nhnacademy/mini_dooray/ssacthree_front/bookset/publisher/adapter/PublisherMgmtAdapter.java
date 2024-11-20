package com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.apache.coyote.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "gateway-service", url = "${admin-client.url}", contextId = "publisherClient")
public interface PublisherMgmtAdapter {
    @GetMapping("/publishers")
    ResponseEntity<Page<PublisherGetResponse>> getAllPublishers(@RequestParam("page") int page,
                                                                @RequestParam("size") int size,
                                                                @RequestParam("sort") String[] sort);

    @GetMapping("/publishers/lists")
    ResponseEntity<List<PublisherGetResponse>> getAllPublisherList();


    @PostMapping("/publishers")
    ResponseEntity<MessageResponse> createPublisher(@RequestBody PublisherCreateRequest publisherCreateRequest);

    @PutMapping("/publishers")
    ResponseEntity<MessageResponse> updatePublisher(@RequestBody PublisherUpdateRequest publisherUpdateRequest);
}
