package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.impl;


import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.adapter.TagMgmtAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.TagMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import feign.FeignException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagMgmtServiceImpl implements TagMgmtService {

    private final TagMgmtAdapter tagMgmtAdapter;

    @Override
    public List<TagInfoResponse> getAllTags() {

        try {
            ResponseEntity<List<TagInfoResponse>> response = tagMgmtAdapter.getAllTags();
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
            ResponseEntity<MessageResponse> response = tagMgmtAdapter.createTag(tagCreateRequest);
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
