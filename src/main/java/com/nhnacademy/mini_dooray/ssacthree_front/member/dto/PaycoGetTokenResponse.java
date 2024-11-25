package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaycoGetTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("access_token_secret")
    private String accessTokenSecret;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("expires_in")
    private String expiresIn;

    @JsonProperty("state")
    private String state;
}
