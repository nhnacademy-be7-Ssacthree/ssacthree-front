package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.adapter.PointSaveRuleCustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.exception.PointSaveRuleGetFailedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PointSaveRuleCustomerServiceImplTest {

    @Mock
    private PointSaveRuleCustomerAdapter pointSaveRuleCustomerAdapter;

    @InjectMocks
    private PointSaveRuleCustomerServiceImpl pointSaveRuleCustomerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPointSaveRuleByRuleName_Success() {
        // Arrange
        String ruleName = "Test Rule";
        PointSaveRuleInfoResponse mockResponse = new PointSaveRuleInfoResponse(1L, ruleName, 100,
            "FIXED");
        when(pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName(ruleName))
            .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Act
        PointSaveRuleInfoResponse result = pointSaveRuleCustomerService.getPointSaveRuleByRuleName(
            ruleName);

        // Assert
        assertNotNull(result);
        assertEquals(ruleName, result.getPointSaveRuleName());
        assertEquals(100, result.getPointSaveAmount());
        verify(pointSaveRuleCustomerAdapter, times(1)).getPointSaveRuleByRuleName(ruleName);
    }

    @Test
    void getPointSaveRuleByRuleName_FailedRequest() {
        // Arrange
        String ruleName = "Invalid Rule";
        when(pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName(ruleName))
            .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        // Act & Assert
        PointSaveRuleGetFailedException exception = assertThrows(
            PointSaveRuleGetFailedException.class,
            () -> pointSaveRuleCustomerService.getPointSaveRuleByRuleName(ruleName));
        assertEquals("적립정책의 이름으로 포인트 적립 정책 조회에 실패하였습니다.", exception.getMessage());
        verify(pointSaveRuleCustomerAdapter, times(1)).getPointSaveRuleByRuleName(ruleName);
    }

    @Test
    void getPointSaveRuleByRuleName_ExceptionThrown() {
        // Arrange
        String ruleName = "Error Rule";
        when(pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName(ruleName))
            .thenThrow(new RuntimeException("Internal Error"));

        // Act & Assert
        PointSaveRuleGetFailedException exception = assertThrows(
            PointSaveRuleGetFailedException.class,
            () -> pointSaveRuleCustomerService.getPointSaveRuleByRuleName(ruleName));
        assertEquals("적립정책의 이름으로 포인트 적립 정책 조회에 실패하였습니다.", exception.getMessage());
        verify(pointSaveRuleCustomerAdapter, times(1)).getPointSaveRuleByRuleName(ruleName);
    }

    @Test
    void getBookPointSaveRule_Success() {
        // Arrange
        String ruleName = "도서 구매 적립";
        PointSaveRuleInfoResponse mockResponse = new PointSaveRuleInfoResponse(2L, ruleName, 200,
            "PERCENTAGE");
        when(pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName(ruleName))
            .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Act
        PointSaveRuleInfoResponse result = pointSaveRuleCustomerService.getBookPointSaveRule();

        // Assert
        assertNotNull(result);
        assertEquals(ruleName, result.getPointSaveRuleName());
        assertEquals(200, result.getPointSaveAmount());
        verify(pointSaveRuleCustomerAdapter, times(1)).getPointSaveRuleByRuleName(ruleName);
    }

    @Test
    void getBookPointSaveRule_ExceptionThrown() {
        // Arrange
        String ruleName = "도서 구매 적립";
        when(pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName(ruleName))
            .thenThrow(new RuntimeException("Internal Error"));

        // Act & Assert
        PointSaveRuleGetFailedException exception = assertThrows(
            PointSaveRuleGetFailedException.class,
            () -> pointSaveRuleCustomerService.getBookPointSaveRule());
        assertEquals("책포인트 적립 정책 조회에 실패하였습니다.", exception.getMessage());
        verify(pointSaveRuleCustomerAdapter, times(1)).getPointSaveRuleByRuleName(ruleName);
    }
}
