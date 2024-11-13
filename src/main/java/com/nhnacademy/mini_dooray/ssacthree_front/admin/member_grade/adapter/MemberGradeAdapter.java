package com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.dto.MemberGradeCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.dto.MemberGradeGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gateway-service", url = "${admin-client.url}", contextId = "memberGradeClient")
public interface MemberGradeAdapter {

    @GetMapping("/member-grades")
    ResponseEntity<List<MemberGradeGetResponse>> getAllMemberGrades();

    @PostMapping("/member-grades")
    ResponseEntity<MessageResponse> createMemberGrade(
        @RequestBody MemberGradeCreateRequest memberGradeCreateRequest);

    @DeleteMapping("/member-grades/{memberGradeId}")
    ResponseEntity<MessageResponse> deleteMemberGrade(@PathVariable Long memberGradeId);
}
