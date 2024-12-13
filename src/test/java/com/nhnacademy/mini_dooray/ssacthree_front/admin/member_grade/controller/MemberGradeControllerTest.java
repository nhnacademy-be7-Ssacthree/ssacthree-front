package com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.dto.MemberGradeCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.member_grade.service.MemberGradeService;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberGradeController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberGradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberGradeService memberGradeService;

    @Test
    void testMemberGrade() throws Exception {
        // Arrange
        when(memberGradeService.getAllMemberGrade()).thenReturn(new ArrayList<>());

        // Act & Assert
        mockMvc.perform(get("/admin/member-grades"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/memberGrade/memberGrade"))
            .andExpect(model().attributeExists("memberGrades"));

        verify(memberGradeService, times(1)).getAllMemberGrade();
    }

    @Test
    void testCreateMemberGradeForm() throws Exception {
        mockMvc.perform(get("/admin/member-grades/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/memberGrade/createMemberGrade"));
    }

    @Test
    void testCreateMemberGrade() throws Exception {
        // Arrange
        String memberGradeName = "Gold";
        String memberGradePointSave = "0.15";

        // Act & Assert
        mockMvc.perform(post("/admin/member-grades/create")
                .param("memberGradeName", memberGradeName)
                .param("memberGradePointSave", memberGradePointSave))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/member-grades"));

        // Capture the request object
        ArgumentCaptor<MemberGradeCreateRequest> captor = ArgumentCaptor.forClass(
            MemberGradeCreateRequest.class);
        verify(memberGradeService, times(1)).memberGradeCreate(captor.capture());

        MemberGradeCreateRequest capturedRequest = captor.getValue();
        assertEquals(memberGradeName, capturedRequest.getMemberGradeName());
        assertEquals(Float.valueOf(memberGradePointSave),
            capturedRequest.getMemberGradePointSave());
    }

    @Test
    void testDeleteMemberGrade() throws Exception {
        Long memberGradeId = 1L;

        mockMvc.perform(get("/admin/member-grades/{memberGradeId}/delete", memberGradeId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/member-grades"));

        verify(memberGradeService, times(1)).deleteMemberGrade(memberGradeId);
    }
}
