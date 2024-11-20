package com.nhnacademy.mini_dooray.ssacthree_front.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ErrorResponse {

    private String message;
    private Integer statusCode;
}
