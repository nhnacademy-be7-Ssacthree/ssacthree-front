package com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.adapter.MemberGradeAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.dto.MemberGradeCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.dto.MemberGradeGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.exception.MemberGradeCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.exception.MemberGradeDeleteFailException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.exception.MemberGradeGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.service.MemberGradeService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import feign.FeignException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberGradeServiceImpl implements MemberGradeService {

    private final MemberGradeAdapter memberGradeAdapter;

    @Override
    public MessageResponse deleteMemberGrade(Long memberGradeId) {

        try {
            ResponseEntity<MessageResponse> response = memberGradeAdapter.deleteMemberGrade(
                memberGradeId);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new MemberGradeDeleteFailException("삭제에 실패하였습니다.");
        } catch (FeignException e) {
            throw new MemberGradeDeleteFailException("삭제에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse memberGradeCreate(MemberGradeCreateRequest memberGradeCreateRequest) {

        try {
            ResponseEntity<MessageResponse> response = memberGradeAdapter.createMemberGrade(
                memberGradeCreateRequest);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new MemberGradeCreateFailedException("회원 등급 생성에 실패하였습니다.");
        } catch (FeignException e) {
            throw new MemberGradeCreateFailedException("회원 등급 생성에 실패하였습니다.");
        }

    }

    @Override
    public List<MemberGradeGetResponse> getAllMemberGrade() {

        try {
            ResponseEntity<List<MemberGradeGetResponse>> response = memberGradeAdapter.getAllMemberGrades();
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new MemberGradeGetFailedException("회원 등급을 조회를 실패하였습니다.");
        } catch (FeignException e) {
            throw new MemberGradeGetFailedException("회원 등급을 조회를 실패하였습니다.");
        }
    }
}
