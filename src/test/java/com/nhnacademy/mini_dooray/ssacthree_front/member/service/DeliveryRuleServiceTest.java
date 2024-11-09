package com.nhnacademy.mini_dooray.ssacthree_front.member.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.adapter.DeliveryRuleAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.exception.DeliveryRuleCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.exception.DeliveryRuleGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.exception.DeliveryRuleUpdateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service.impl.DeliveryRuleServiceImpl;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeliveryRuleServiceTest {

    @Mock
    private DeliveryRuleAdapter deliveryRuleAdapter;

    @InjectMocks
    private DeliveryRuleServiceImpl deliveryRuleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDeliveryRule_success() {
        DeliveryRuleCreateRequest request = new DeliveryRuleCreateRequest();
        MessageResponse messageResponse = new MessageResponse("성공적으로 생성되었습니다.");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(messageResponse, HttpStatus.OK);

        when(deliveryRuleAdapter.createDeliveryRule(request)).thenReturn(responseEntity);

        MessageResponse result = deliveryRuleService.createDeliveryRule(request);
        assertEquals("성공적으로 생성되었습니다.", result.getMessage());
    }

    @Test
    void testGetAllDeliveryRules_success() {
        DeliveryRuleGetResponse response1 = new DeliveryRuleGetResponse();
        DeliveryRuleGetResponse response2 = new DeliveryRuleGetResponse();
        List<DeliveryRuleGetResponse> responseList = List.of(response1, response2);
        ResponseEntity<List<DeliveryRuleGetResponse>> responseEntity = new ResponseEntity<>(responseList, HttpStatus.OK);

        when(deliveryRuleAdapter.getAllDeliveryRules()).thenReturn(responseEntity);

        List<DeliveryRuleGetResponse> result = deliveryRuleService.getAllDeliveryRules();
        assertEquals(2, result.size());
        assertEquals(responseList, result);
    }

    @Test
    void testUpdateDeliveryRule_success() {
        DeliveryRuleUpdateRequest request = new DeliveryRuleUpdateRequest(/*필요한 필드 설정*/);
        MessageResponse messageResponse = new MessageResponse("성공적으로 수정되었습니다.");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(messageResponse, HttpStatus.OK);

        when(deliveryRuleAdapter.updateDeliveryRule(request)).thenReturn(responseEntity);

        MessageResponse result = deliveryRuleService.updateDeliveryRule(request);
        assertEquals("성공적으로 수정되었습니다.", result.getMessage());
    }

}
