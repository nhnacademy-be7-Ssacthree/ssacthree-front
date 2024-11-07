package com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto.PublisherCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto.PublisherGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto.PublisherUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;

import java.util.List;

public interface PublisherService {
    List<PublisherGetResponse> getAllPublishers();

    MessageResponse createPublisher(PublisherCreateRequest publisherCreateRequest);

    MessageResponse updatePublisher(PublisherUpdateRequest publisherUpdateRequest);
}
