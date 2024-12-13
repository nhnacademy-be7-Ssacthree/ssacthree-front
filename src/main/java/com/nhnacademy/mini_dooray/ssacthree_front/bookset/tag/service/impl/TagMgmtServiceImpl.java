package com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.impl;


import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.adapter.TagMgmtAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.dto.TagUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.exception.TagFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.tag.service.TagMgmtService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagMgmtServiceImpl implements TagMgmtService {
    private static final String TAG_NOT_FOUND_MESSAGE = "태그 아이디 조회에 실패했습니다.";
    private static final String TAG_SEARCH_ERROR_MESSAGE = "태그 조회에 실패했습니다.";
    private static final String TAG_CREATE_ERROR_MESSAGE = "태그 생성에 실패했습니다.";
    private static final String TAG_UPDATE_ERROR_MESSAGE = "태그 수정에 실패했습니다.";
    private static final String TAG_DELETE_ERROR_MESSAGE = "태그 삭제에 실패했습니다.";

    private final TagMgmtAdapter tagMgmtAdapter;

    @Override
    public Page<TagInfoResponse> getAllTags(int page, int size, String[] sort) {

        try {
            ResponseEntity<Page<TagInfoResponse>> response = tagMgmtAdapter.getAllTags(page, size, sort);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new TagFailedException(TAG_SEARCH_ERROR_MESSAGE);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw new TagFailedException(TAG_SEARCH_ERROR_MESSAGE);
        }
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
            throw new TagFailedException(TAG_CREATE_ERROR_MESSAGE);
        } catch (HttpServerErrorException | HttpClientErrorException e) {
            throw new TagFailedException(TAG_CREATE_ERROR_MESSAGE);
        }
    }

    @Override
    public MessageResponse updateTag(TagUpdateRequest tagUpdateRequest) {
        ResponseEntity<MessageResponse> response = tagMgmtAdapter.updateTag(tagUpdateRequest);

        try{
            if(response.getStatusCode().is2xxSuccessful()){
                return response.getBody();
            }
            throw new TagFailedException(TAG_UPDATE_ERROR_MESSAGE);
        }catch (HttpServerErrorException | HttpClientErrorException e){
            throw new TagFailedException(TAG_UPDATE_ERROR_MESSAGE);
        }
    }

    @Override
    public MessageResponse deleteTag(Long tagId) {
        ResponseEntity<MessageResponse> response = tagMgmtAdapter.deleteTag(tagId);

        try{
            if(response.getStatusCode().is2xxSuccessful()){
                return response.getBody();
            }
            throw new TagFailedException(TAG_DELETE_ERROR_MESSAGE);
        }catch (HttpServerErrorException | HttpClientErrorException e){
            throw new TagFailedException(TAG_DELETE_ERROR_MESSAGE);
        }
    }
}
