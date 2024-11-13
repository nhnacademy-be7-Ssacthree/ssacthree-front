package com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.service;


import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.dto.MemberGradeCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.dto.MemberGradeGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import java.util.List;

public interface MemberGradeService {

    MessageResponse memberGradeCreate(MemberGradeCreateRequest memberGradeCreateRequest);

    List<MemberGradeGetResponse> getAllMemberGrade();

    MessageResponse deleteMemberGrade(Long memberGradeId);
}
