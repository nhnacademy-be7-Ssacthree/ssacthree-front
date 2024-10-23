package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberRegisterRequest {

    private String loginId;
    private String loginPassword;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
    private String birth;

}
