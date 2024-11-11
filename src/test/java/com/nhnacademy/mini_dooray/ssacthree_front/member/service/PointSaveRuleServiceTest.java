package com.nhnacademy.mini_dooray.ssacthree_front.member.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.adapter.PointSaveRuleAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.exception.PointSaveRuleCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.exception.PointSaveRuleGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.exception.PointSaveRuleUpdateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.impl.PointSaveRuleServiceImpl;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class PointSaveRuleServiceTest {

    @Mock
    private PointSaveRuleAdapter pointSaveRuleAdapter;

    @InjectMocks
    private PointSaveRuleServiceImpl pointSaveRuleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPointSaveRules_Success() {
        PointSaveRuleGetResponse response = new PointSaveRuleGetResponse();
        when(pointSaveRuleAdapter.getAllPointSaveRules())
            .thenReturn(new ResponseEntity<>(Collections.singletonList(response), HttpStatus.OK));

        List<PointSaveRuleGetResponse> result = pointSaveRuleService.getAllPointSaveRules();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllPointSaveRules_Failure() {
    }

    @Test
    void testCreatePointSaveRule_Success() {
        PointSaveRuleCreateRequest request = new PointSaveRuleCreateRequest();
        MessageResponse messageResponse = new MessageResponse("Created successfully");

        when(pointSaveRuleAdapter.createPointSaveRule(any(PointSaveRuleCreateRequest.class)))
            .thenReturn(new ResponseEntity<>(messageResponse, HttpStatus.OK));

        MessageResponse response = pointSaveRuleService.createPointSaveRule(request);
        assertNotNull(response);
        assertEquals("Created successfully", response.getMessage());
    }

    @Test
    void testCreatePointSaveRule_Failure() {
    }

    @Test
    void testUpdatePointSaveRule_Success() {
        PointSaveRuleUpdateRequest request = new PointSaveRuleUpdateRequest();
        MessageResponse messageResponse = new MessageResponse("Updated successfully");

        when(pointSaveRuleAdapter.updatePointSaveRule(any(PointSaveRuleUpdateRequest.class)))
            .thenReturn(new ResponseEntity<>(messageResponse, HttpStatus.OK));

        MessageResponse response = pointSaveRuleService.updatePointSaveRule(request);
        assertNotNull(response);
        assertEquals("Updated successfully", response.getMessage());
    }

    @Test
    void testUpdatePointSaveRule_Failure() {
    }
}
