package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.adapter;


import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "gateway-service", url = "${admin-client.url}", contextId = "adminTagClient")
public interface TagMgmtAdapter {

    @PostMapping("/tags")
    ResponseEntity<MessageResponse> createTag(@RequestBody TagCreateRequest tagCreateRequest);

    @GetMapping("/tags")
    ResponseEntity<Page<TagInfoResponse>> getAllTags(@RequestParam("page") int page,
                                                     @RequestParam("size") int size,
                                                     @RequestParam("sort") String[] sort);
}
