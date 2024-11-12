package com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.service;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;

import java.util.List;

public interface PublisherMgmtService {
    List<PublisherGetResponse> getAllPublishers();

    MessageResponse createPublisher(PublisherCreateRequest publisherCreateRequest);

    MessageResponse updatePublisher(PublisherUpdateRequest publisherUpdateRequest);
}
