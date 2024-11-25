package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.adapter;


import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "gateway-service", url = "${admin-client.url}", contextId = "adminTagClient")
public interface TagMgmtAdapter {

    @PostMapping("/tags")
    ResponseEntity<MessageResponse> createTag(@RequestBody TagCreateRequest tagCreateRequest);

    @GetMapping("/tags")
    ResponseEntity<Page<TagInfoResponse>> getAllTags(@RequestParam("page") int page,
                                                     @RequestParam("size") int size,
                                                     @RequestParam("sort") String[] sort);

    @GetMapping("/tags/lists")
    ResponseEntity<List<TagInfoResponse>> getAllTagList();

    @PutMapping("/tags")
    ResponseEntity<MessageResponse> updateTag(@RequestBody TagUpdateRequest tagUpdateRequest);

    @DeleteMapping("/tags/{tag-id}")
    ResponseEntity<MessageResponse> deleteTag(@PathVariable(name = "tag-id") Long tagId);

}
