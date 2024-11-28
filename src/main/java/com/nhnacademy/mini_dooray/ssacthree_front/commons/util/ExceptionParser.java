package com.nhnacademy.mini_dooray.ssacthree_front.commons.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.ErrorResponse;


public class ExceptionParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private ExceptionParser() {
    }

    /**
     * FeignException의 메세지를 빼내는 메소드
     *
     * @param errorMessageBody
     * @return
     */
    public static String getErrorMessageFromFeignException(String errorMessageBody) {

        try {
            return objectMapper.readValue(errorMessageBody, ErrorResponse.class).getMessage();
        } catch (JsonProcessingException e) {
            return errorMessageBody;
        }

    }

    public static String getErrorCodeFromFeignException(String errorMessageBody) {
        try {
            return Integer.toString(
                objectMapper.readValue(errorMessageBody, ErrorResponse.class).getStatusCode());
        } catch (JsonProcessingException e) {
            return errorMessageBody;
        }
    }
}
