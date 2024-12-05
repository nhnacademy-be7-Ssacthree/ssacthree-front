package com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.adapter.DeliveryRuleAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.adapter.DeliveryRuleCustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

class DeliveryRuleServiceImplTest {

    private DeliveryRuleAdapter deliveryRuleAdapter;
    private DeliveryRuleCustomerAdapter deliveryRuleCustomerAdapter;
    private DeliveryRuleServiceImpl deliveryRuleService;

    @BeforeEach
    void setUp() {
        deliveryRuleAdapter = Mockito.mock(DeliveryRuleAdapter.class);
        deliveryRuleCustomerAdapter = Mockito.mock(DeliveryRuleCustomerAdapter.class);
        deliveryRuleService = new DeliveryRuleServiceImpl(deliveryRuleAdapter, deliveryRuleCustomerAdapter);
    }

    @Test
    @DisplayName("createDeliveryRule 성공 케이스")
    void testCreateDeliveryRuleSuccess() {
        DeliveryRuleCreateRequest request = new DeliveryRuleCreateRequest();
        request.setDeliveryRuleName("Test Rule");
        request.setDeliveryFee(5000);
        request.setDeliveryDiscountCost(1000);

        MessageResponse response = new MessageResponse("Rule created successfully");
        Mockito.when(deliveryRuleAdapter.createDeliveryRule(any()))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        MessageResponse result = deliveryRuleService.createDeliveryRule(request);

        assertThat(result.getMessage()).isEqualTo("Rule created successfully");
    }

//    @Test
//    @DisplayName("createDeliveryRule 실패 케이스")
//    void testCreateDeliveryRuleFailure() {
//        DeliveryRuleCreateRequest request = new DeliveryRuleCreateRequest();
//        request.setDeliveryRuleName("Test Rule");
//        request.setDeliveryFee(5000);
//        request.setDeliveryDiscountCost(1000);
//
//        Mockito.when(deliveryRuleAdapter.createDeliveryRule(any()))
//                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
//
//        assertThrows(DeliveryRuleCreateFailedException.class, () -> deliveryRuleService.createDeliveryRule(request));
//    }

    @Test
    @DisplayName("getAllDeliveryRules 성공 케이스")
    void testGetAllDeliveryRulesSuccess() {
        List<DeliveryRuleGetResponse> responses = List.of(
                new DeliveryRuleGetResponse(1L, "Rule1", 5000, 1000, true, LocalDateTime.now())
        );

        Mockito.when(deliveryRuleAdapter.getAllDeliveryRules())
                .thenReturn(new ResponseEntity<>(responses, HttpStatus.OK));

        List<DeliveryRuleGetResponse> result = deliveryRuleService.getAllDeliveryRules();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getDeliveryRuleName()).isEqualTo("Rule1");
    }

//    @Test
//    @DisplayName("getAllDeliveryRules 실패 케이스")
//    void testGetAllDeliveryRulesFailure() {
//        Mockito.when(deliveryRuleAdapter.getAllDeliveryRules())
//                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
//
//        assertThrows(DeliveryRuleGetFailedException.class, deliveryRuleService::getAllDeliveryRules);
//    }

    @Test
    @DisplayName("updateDeliveryRule 성공 케이스")
    void testUpdateDeliveryRuleSuccess() {
        DeliveryRuleUpdateRequest request = new DeliveryRuleUpdateRequest(1L);
        MessageResponse response = new MessageResponse("Rule updated successfully");

        Mockito.when(deliveryRuleAdapter.updateDeliveryRule(any()))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        MessageResponse result = deliveryRuleService.updateDeliveryRule(request);

        assertThat(result.getMessage()).isEqualTo("Rule updated successfully");
    }

//    @Test
//    @DisplayName("updateDeliveryRule 실패 케이스")
//    void testUpdateDeliveryRuleFailure() {
//        DeliveryRuleUpdateRequest request = new DeliveryRuleUpdateRequest(1L);
//
//        Mockito.when(deliveryRuleAdapter.updateDeliveryRule(any()))
//                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
//
//        assertThrows(DeliveryRuleUpdateFailedException.class, () -> deliveryRuleService.updateDeliveryRule(request));
//    }

    @Test
    @DisplayName("getCurrentDeliveryRule 성공 케이스")
    void testGetCurrentDeliveryRuleSuccess() {
        DeliveryRuleGetResponse response = new DeliveryRuleGetResponse(
                1L, "Current Rule", 5000, 1000, true, LocalDateTime.now());

        Mockito.when(deliveryRuleCustomerAdapter.getCurrentDeliveryRule())
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        DeliveryRuleGetResponse result = deliveryRuleService.getCurrentDeliveryRule();

        assertThat(result.getDeliveryRuleName()).isEqualTo("Current Rule");
    }

//    @Test
//    @DisplayName("getCurrentDeliveryRule 실패 케이스")
//    void testGetCurrentDeliveryRuleFailure() {
//        Mockito.when(deliveryRuleCustomerAdapter.getCurrentDeliveryRule())
//                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR));
//
//        assertThrows(DeliveryRuleGetFailedException.class, deliveryRuleService::getCurrentDeliveryRule);
//    }
}
