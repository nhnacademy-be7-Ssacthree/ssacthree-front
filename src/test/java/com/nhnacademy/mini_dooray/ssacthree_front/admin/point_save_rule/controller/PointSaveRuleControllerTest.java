package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.PointSaveRuleService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PointSaveRuleController.class)
class PointSaveRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointSaveRuleService pointSaveRuleService;

    @Test
    @DisplayName("GET /admin/point-save-rules - 포인트 적립 규칙 조회")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testGetPointSaveRules() throws Exception {
        mockMvc.perform(get("/admin/point-save-rules"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("pointSaveRules"))
                .andExpect(view().name("admin/pointSaveRule/pointSaveRules"));
    }

    @Test
    @DisplayName("POST /admin/point-save-rules - 포인트 적립 규칙 업데이트")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testUpdatePointSaveRule() throws Exception {
        mockMvc.perform(post("/admin/point-save-rules")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("pointSaveRuleId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/point-save-rules"));

        Mockito.verify(pointSaveRuleService).updatePointSaveRule(any(PointSaveRuleUpdateRequest.class));
    }

    @Test
    @DisplayName("GET /admin/point-save-rules/create - 포인트 적립 규칙 생성 페이지")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testCreatePointSaveRulePage() throws Exception {
        mockMvc.perform(get("/admin/point-save-rules/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/pointSaveRule/createPointSaveRule"));
    }

    @Test
    @DisplayName("POST /admin/point-save-rules/create - 포인트 적립 규칙 생성")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testCreatePointSaveRule() throws Exception {
        mockMvc.perform(post("/admin/point-save-rules/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("pointSaveRuleName", "New Rule")
                        .param("pointSaveAmount", "100")
                        .param("pointSaveType", "PERCENT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/point-save-rules"));

        Mockito.verify(pointSaveRuleService).createPointSaveRule(any(PointSaveRuleCreateRequest.class));
    }
}
