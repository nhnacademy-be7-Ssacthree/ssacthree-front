package com.nhnacademy.mini_dooray.ssacthree_front.member.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResponse {

    private String memberLoginId;
    private String customerName;
    private String customerPhoneNumber;
    private String customerEmail;
    private String memberBirthdate;
    private long memberPoint;
    private String memberGradeName;
    private float memberGradePointSave;

}
