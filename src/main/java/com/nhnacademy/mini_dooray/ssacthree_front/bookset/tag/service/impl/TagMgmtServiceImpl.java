package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.impl;


import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.adapter.TagMgmtAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.exception.TagFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.TagMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagMgmtServiceImpl implements TagMgmtService {
    private static final String TAG_NOT_FOUND_MESSAGE = "태그 아이디 조회에 실패했습니다.";

    private final TagMgmtAdapter tagMgmtAdapter;

    @Override
    public Page<TagInfoResponse> getAllTags(int page, int size, String[] sort) {

        try {
            ResponseEntity<Page<TagInfoResponse>> response = tagMgmtAdapter.getAllTags(page, size, sort);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
        } catch (FeignException e) {
            // todo: 이렇게 해도 될지 모르겠음. 차라리 예외 처리를 하는 것이..?
            return (Page<TagInfoResponse>) Collections.EMPTY_LIST;
        }

        // todo: 이렇게 해도 될지 모르겠음. 차라리 예외 처리를 하는 것이..?
        return (Page<TagInfoResponse>) Collections.EMPTY_LIST;
    }

    @Override
    public List<TagInfoResponse> getAllTagList(){
        try{
            ResponseEntity<List<TagInfoResponse>> response = tagMgmtAdapter.getAllTagList();
            if(response.getStatusCode().is2xxSuccessful()){
                return response.getBody();
            }
            throw new TagFailedException(TAG_NOT_FOUND_MESSAGE);
        }catch (HttpServerErrorException | HttpClientErrorException e){
            throw new TagFailedException(TAG_NOT_FOUND_MESSAGE);
        }
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
