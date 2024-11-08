package com.nhnacademy.mini_dooray.ssacthree_front.admin.tag.service;


import com.nhnacademy.mini_dooray.ssacthree_front.admin.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.tag.dto.TagGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import java.util.List;

public interface TagService {

    List<TagGetResponse> getAllTags();

    MessageResponse createTag(TagCreateRequest tagCreateRequest);

}
