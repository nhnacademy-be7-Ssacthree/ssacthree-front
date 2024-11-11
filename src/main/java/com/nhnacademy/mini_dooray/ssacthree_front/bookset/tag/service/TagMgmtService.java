package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service;


import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import java.util.List;

public interface TagMgmtService {

    List<TagInfoResponse> getAllTags();

    MessageResponse createTag(TagCreateRequest tagCreateRequest);

}
