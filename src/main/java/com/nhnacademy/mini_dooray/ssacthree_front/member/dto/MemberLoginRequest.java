package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MemberLoginRequest {
    // 아직도 불필요한 어노테이션이 이렇게나 많이.....
    // MemberRegisterRequest에도 불필요한 어노테이션이....
    private String memberLoginId;
    private String memberPassword;
}
