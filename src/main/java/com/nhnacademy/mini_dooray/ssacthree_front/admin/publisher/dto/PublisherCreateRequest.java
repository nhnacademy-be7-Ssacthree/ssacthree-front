package com.nhnacademy.mini_dooray.ssacthree_front.admin.publisher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PublisherCreateRequest {

    @NotBlank
    @Size(max = 30)
    private String publisherName;
}
