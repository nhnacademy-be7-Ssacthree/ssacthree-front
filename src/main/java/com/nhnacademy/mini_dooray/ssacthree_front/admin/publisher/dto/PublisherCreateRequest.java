package com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PublisherCreateRequest {

    @NotBlank
    private String publisherName;
}
