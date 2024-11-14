package com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.dto.MemberGradeCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.service.MemberGradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping(("/admin/member-grades"))
public class MemberGradeController {

    private final MemberGradeService memberGradeService;

    @GetMapping
    public String memberGrade(Model model) {
        model.addAttribute("memberGrades", memberGradeService.getAllMemberGrade());
        return "admin/memberGrade/memberGrade";
    }

    @GetMapping("/create")
    public String createMemberGrade() {
        return "admin/memberGrade/createMemberGrade";
    }

    @PostMapping("/create")
    public String createMemberGrade(
        @ModelAttribute MemberGradeCreateRequest memberGradeCreateRequest) {
        memberGradeService.memberGradeCreate(memberGradeCreateRequest);
        return "redirect:/admin/member-grades";
    }

    @GetMapping("/{memberGradeId}/delete")
    public String deleteMemberGrade(@PathVariable Long memberGradeId) {
        memberGradeService.deleteMemberGrade(memberGradeId);
        return "redirect:/admin/member-grades";
    }
}
