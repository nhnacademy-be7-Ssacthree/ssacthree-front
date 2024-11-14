package com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberGradeGetResponse {

    private Long memberGradeId;
    private String memberGradeName;
    private Float memberGradePointSave;
    private LocalDateTime memberGradeCreateAt;
    private boolean memberGradeIsUsed;

}
