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
    private String loginPassword;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
    private String birth;

}
