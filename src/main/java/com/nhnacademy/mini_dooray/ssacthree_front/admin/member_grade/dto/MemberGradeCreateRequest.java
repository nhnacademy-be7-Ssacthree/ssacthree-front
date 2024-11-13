package com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemberGradeCreateRequest {

    private String memberGradeName;
    private Float memberGradePointSave;
}
