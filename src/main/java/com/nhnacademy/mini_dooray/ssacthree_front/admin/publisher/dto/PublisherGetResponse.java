package com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherGetResponse {
    private long publisherId;
    private String publisherName;
    private boolean publisherIsUsed;
}
