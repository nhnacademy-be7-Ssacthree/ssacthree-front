package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.controller.PointSaveRuleController;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.PointSaveRuleService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PointSaveRuleController.class)
class PointSaveRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointSaveRuleService pointSaveRuleService;

    @Test
    void testGetAllPointSaveRules() throws Exception {
        when(pointSaveRuleService.getAllPointSaveRules()).thenReturn(
            Collections.singletonList(new PointSaveRuleGetResponse()));

        mockMvc.perform(get("/admin/point-save-rules"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/pointSaveRule/pointSaveRules"))
            .andExpect(model().attributeExists("pointSaveRules"));
    }

//    @Test
//    void testCreatePointSaveRule_Success() throws Exception {
//        mockMvc.perform(post("/admin/point-save-rules/create")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("pointSaveRuleName", "Basic Point Save") // 필수 필드 설정
//                .param("pointSaveAmount", "100") // 필수 필드 설정
//                .param("pointSaveType", PointSaveType.INTEGER.toString())) // enum 값을 문자열로 변환하여 사용
//            .andExpect(status().is3xxRedirection())
//            .andExpect(redirectedUrl("/admin/point-save-rules?accessTokenExists=false")); // 올바른 경로 설정
//
//        verify(pointSaveRuleService, times(1)).createPointSaveRule(any(PointSaveRuleCreateRequest.class));
//    }
//
//    @Test
//    void testUpdatePointSaveRule_Success() throws Exception {
//        mockMvc.perform(post("/admin/point-save-rules")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("pointSaveRuleId", "1"))  // 필수 필드 설정
//            .andExpect(status().is3xxRedirection())
//            .andExpect(redirectedUrl("/admin/point-save-rules?accessTokenExists=false")); // 수정된 경로와 파라미터
//
//        verify(pointSaveRuleService, times(1)).updatePointSaveRule(any(PointSaveRuleUpdateRequest.class));
//    }
}

