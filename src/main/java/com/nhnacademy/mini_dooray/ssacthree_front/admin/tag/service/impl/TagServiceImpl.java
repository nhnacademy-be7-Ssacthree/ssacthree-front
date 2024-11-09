package com.nhnacademy.mini_dooray.ssacthree_front.admin.tag.service.impl;


import com.nhnacademy.mini_dooray.ssacthree_front.admin.tag.adapter.TagAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.tag.dto.TagGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.tag.service.TagService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import feign.FeignException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagAdapter tagAdapter;

    @Override
    public List<TagGetResponse> getAllTags() {

        try {
            ResponseEntity<List<TagGetResponse>> response = tagAdapter.getAllTags();
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (FeignException e) {
            return Collections.EMPTY_LIST;
        }
        
        return Collections.EMPTY_LIST;
    }

    @Override
    public MessageResponse createTag(TagCreateRequest tagCreateRequest) {

        try {
            ResponseEntity<MessageResponse> response = tagAdapter.createTag(tagCreateRequest);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (FeignException e) {
            // TODO : 적절한 예외 만들어야함.
            throw new RuntimeException("태그 생성이 불가능합니다.");
        }
        throw new RuntimeException("태그 생성이 불가능합니다.");
    }
}
