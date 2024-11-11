package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.controller.DeliveryRuleController;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service.DeliveryRuleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DeliveryRuleController.class)
class DeliveryRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DeliveryRuleService deliveryRuleService;

    @Test
    void testGetAllDeliveryRules() throws Exception {
        when(deliveryRuleService.getAllDeliveryRules()).thenReturn(Collections.singletonList(new DeliveryRuleGetResponse()));

        mockMvc.perform(get("/admin/delivery-rules"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/deliveryRule/deliveryRules"))
            .andExpect(model().attributeExists("deliveryRules"));
    }

    @Test
    void testCreateDeliveryRule_Success() throws Exception {
        mockMvc.perform(post("/admin/delivery-rules/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("deliveryRuleName", "Fast Delivery")
                .param("deliveryFee", "5000"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/delivery-rules?accessTokenExists=false"));

        verify(deliveryRuleService, times(1)).createDeliveryRule(any(DeliveryRuleCreateRequest.class));
    }

    @Test
    void testUpdateDeliveryRule_Success() throws Exception {
        mockMvc.perform(post("/admin/delivery-rules")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("deliveryRuleName", "Updated Delivery")
                .param("deliveryFee", "6000"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin/delivery-rules?accessTokenExists=false"));

        verify(deliveryRuleService, times(1)).updateDeliveryRule(any(DeliveryRuleUpdateRequest.class));
    }
}
