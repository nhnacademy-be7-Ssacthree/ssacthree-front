package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service;


import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.data.domain.Page;

public interface TagMgmtService {

    Page<TagInfoResponse> getAllTags(int page, int size, String[] sort);

    MessageResponse createTag(TagCreateRequest tagCreateRequest);

}
