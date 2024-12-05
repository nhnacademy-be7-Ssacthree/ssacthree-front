package com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service.DeliveryRuleService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeliveryRuleController.class)
class DeliveryRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryRuleService deliveryRuleService;

    @Test
    @DisplayName("GET /admin/delivery-rules - 성공적으로 배송 규칙 조회")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testGetDeliveryRules() throws Exception {
        List<DeliveryRuleGetResponse> responses = List.of(
                new DeliveryRuleGetResponse(1L, "Rule1", 5000, 1000, true, LocalDateTime.now()),
                new DeliveryRuleGetResponse(2L, "Rule2", 3000, 500, false, LocalDateTime.now())
        );

        BDDMockito.given(deliveryRuleService.getAllDeliveryRules()).willReturn(responses);

        mockMvc.perform(get("/admin/delivery-rules")
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("deliveryRules"))
                .andExpect(model().attributeExists("paging"))
                .andExpect(view().name("admin/deliveryRule/deliveryRules"));
    }

    @Test
    @DisplayName("POST /admin/delivery-rules - 배송 규칙 업데이트")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testUpdateDeliveryRule() throws Exception {
        DeliveryRuleUpdateRequest request = new DeliveryRuleUpdateRequest(1L);

        mockMvc.perform(post("/admin/delivery-rules")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("deliveryRuleId", String.valueOf(request.getDeliveryRuleId())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/delivery-rules"));

        Mockito.verify(deliveryRuleService).updateDeliveryRule(any(DeliveryRuleUpdateRequest.class));
    }

    @Test
    @DisplayName("GET /admin/delivery-rules/create - 배송 규칙 생성 페이지")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testCreateDeliveryRulePage() throws Exception {
        mockMvc.perform(get("/admin/delivery-rules/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/deliveryRule/createDeliveryRule"));
    }

    @Test
    @DisplayName("POST /admin/delivery-rules/create - 배송 규칙 생성")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void testCreateDeliveryRule() throws Exception {
        DeliveryRuleCreateRequest request = new DeliveryRuleCreateRequest();
        request.setDeliveryRuleName("New Rule");
        request.setDeliveryFee(5000);
        request.setDeliveryDiscountCost(1000);

        mockMvc.perform(post("/admin/delivery-rules/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("deliveryRuleName", request.getDeliveryRuleName())
                        .param("deliveryFee", String.valueOf(request.getDeliveryFee()))
                        .param("deliveryDiscountCost", String.valueOf(request.getDeliveryDiscountCost())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/delivery-rules"));

        Mockito.verify(deliveryRuleService).createDeliveryRule(any(DeliveryRuleCreateRequest.class));
    }
}
