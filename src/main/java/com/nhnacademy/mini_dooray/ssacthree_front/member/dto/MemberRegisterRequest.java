package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberRegisterRequest {


    @NotBlank
    @Size(min = 5, max = 20)
    private String loginId;

    @NotBlank
    @Size(min = 8, max = 20)
    //비밀번호는 8글자에서 20글자만 받자!
    private String loginPassword;

    @NotBlank
    @Size(max = 10)
    // 10글자만 주기로 함.
    private String customerName;

    @NotBlank
    @Size(max = 13)
    private String customerPhoneNumber;

    @NotBlank
    @Size(max = 50)
    private String customerEmail;

    @NotBlank
    @Size(max = 8)
    private String birth;

}
