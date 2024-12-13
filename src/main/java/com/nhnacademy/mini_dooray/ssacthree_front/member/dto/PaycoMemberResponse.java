package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaycoMemberResponse {

    private Header header;
    private Data data;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Header {

        @JsonProperty("isSuccessful")
        private boolean isSuccessful;

        private int resultCode;
        private String resultMessage;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Data {

        private Member member;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Member {

        private String idNo;
    }
}
