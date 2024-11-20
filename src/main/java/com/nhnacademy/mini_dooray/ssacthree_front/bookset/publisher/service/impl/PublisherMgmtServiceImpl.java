package com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.adapter.PublisherMgmtAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.exception.PublisherCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.exception.PublisherGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.exception.PublisherUpdateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.service.PublisherMgmtService;
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
public class PublisherMgmtServiceImpl implements PublisherMgmtService {

    private final PublisherMgmtAdapter publisherMgmtAdapter;

    @Override
    public Page<PublisherGetResponse> getAllPublishers(int page, int size, String[] sort) {
        ResponseEntity<Page<PublisherGetResponse>> response = publisherMgmtAdapter.getAllPublishers(page, size, sort);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new PublisherGetFailedException("출판사 조회에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PublisherGetFailedException("출판사 조회에 실패하였습니다.");
        }
    }

    @Override
    public List<PublisherGetResponse> getAllPublisherList(){
        ResponseEntity<List<PublisherGetResponse>> response = publisherMgmtAdapter.getAllPublisherList();

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new PublisherGetFailedException("출판사 조회에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PublisherGetFailedException("출판사 조회에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse createPublisher(PublisherCreateRequest publisherCreateRequest) {
        ResponseEntity<MessageResponse> response = publisherMgmtAdapter.createPublisher(publisherCreateRequest);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new PublisherCreateFailedException("출판사 생성에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PublisherCreateFailedException("출판사 생성에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse updatePublisher(PublisherUpdateRequest publisherUpdateRequest) {
        ResponseEntity<MessageResponse> response = publisherMgmtAdapter.updatePublisher(publisherUpdateRequest);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new PublisherUpdateFailedException("출판사 수정에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PublisherUpdateFailedException("출판사 수정에 실패하였습니다.");
        }
    }
}
