package com.nhnacademy.mini_dooray.ssacthree_front.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminLoginRequest {

    String loginId;
    String password;
}
