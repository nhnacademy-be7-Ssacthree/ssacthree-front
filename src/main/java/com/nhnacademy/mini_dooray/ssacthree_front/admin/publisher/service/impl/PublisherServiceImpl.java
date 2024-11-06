package com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.adapter.PublisherAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto.PublisherDeleteRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto.PublisherGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.exception.PublisherCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.exception.PublisherDeleteFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.exception.PublisherGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.exception.PublisherUpdateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.service.PublisherService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

    private final PublisherAdapter publisherAdapter;

    @Override
    public List<PublisherGetResponse> getAllPublishers() {
        ResponseEntity<List<PublisherGetResponse>> response = publisherAdapter.getAllPublishers();

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
        ResponseEntity<MessageResponse> response = publisherAdapter.createPublisher(publisherCreateRequest);

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
        ResponseEntity<MessageResponse> response = publisherAdapter.updatePublisher(publisherUpdateRequest);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new PublisherUpdateFailedException("출판사 수정에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PublisherUpdateFailedException("출판사 수정에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse deletePublisher(PublisherDeleteRequest publisherDeleteRequest) {
        ResponseEntity<MessageResponse> response = publisherAdapter.deletePublisher(publisherDeleteRequest);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new PublisherDeleteFailedException("출판사 삭제에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PublisherDeleteFailedException("출판사 삭제에 실패하였습니다.");
        }
    }
}
