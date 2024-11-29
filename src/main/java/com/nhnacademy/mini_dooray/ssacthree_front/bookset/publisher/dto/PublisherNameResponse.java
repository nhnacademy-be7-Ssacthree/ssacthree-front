package com.nhnacademy.mini_dooray.ssacthree_front.bookset.publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PublisherNameResponse {
    private Long publisherId;
    private String publisherName;
}
