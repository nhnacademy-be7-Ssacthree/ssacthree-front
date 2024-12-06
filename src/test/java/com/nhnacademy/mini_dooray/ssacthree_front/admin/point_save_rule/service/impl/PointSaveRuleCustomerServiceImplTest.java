package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.adapter.PointSaveRuleCustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.exception.PointSaveRuleGetFailedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointSaveRuleCustomerServiceImplTest {

    @Mock
    private PointSaveRuleCustomerAdapter pointSaveRuleCustomerAdapter;

    @InjectMocks
    private PointSaveRuleCustomerServiceImpl pointSaveRuleCustomerService;

    @Test
    void getPointSaveRuleByRuleName_Success() {
        // Arrange
        String ruleName = "도서 구매 적립";
        PointSaveRuleInfoResponse mockResponse = new PointSaveRuleInfoResponse(1L, ruleName, 100, "FIXED");
        when(pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName(ruleName))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Act
        PointSaveRuleInfoResponse result = pointSaveRuleCustomerService.getPointSaveRuleByRuleName(ruleName);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getPointSaveRuleName()).isEqualTo(ruleName);
        assertThat(result.getPointSaveAmount()).isEqualTo(100);
        verify(pointSaveRuleCustomerAdapter, times(1)).getPointSaveRuleByRuleName(ruleName);
    }

    @Test
    void getPointSaveRuleByRuleName_FailedWithNon2xxStatus() {
        // Arrange
        String ruleName = "도서 구매 적립";
        when(pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName(ruleName))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST));

        // Act & Assert
        assertThatThrownBy(() -> pointSaveRuleCustomerService.getPointSaveRuleByRuleName(ruleName))
                .isInstanceOf(PointSaveRuleGetFailedException.class)
                .hasMessage("적립정책의 이름으로 포인트 적립 정책 조회에 실패하였습니다.");
        verify(pointSaveRuleCustomerAdapter, times(1)).getPointSaveRuleByRuleName(ruleName);
    }

    @Test
    void getPointSaveRuleByRuleName_FailedWithException() {
        // Arrange
        String ruleName = "도서 구매 적립";
        when(pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName(ruleName))
                .thenThrow(new RuntimeException("Internal Error"));

        // Act & Assert
        assertThatThrownBy(() -> pointSaveRuleCustomerService.getPointSaveRuleByRuleName(ruleName))
                .isInstanceOf(PointSaveRuleGetFailedException.class)
                .hasMessage("적립정책의 이름으로 포인트 적립 정책 조회에 실패하였습니다.");
        verify(pointSaveRuleCustomerAdapter, times(1)).getPointSaveRuleByRuleName(ruleName);
    }

    @Test
    void getBookPointSaveRule_Success() {
        // Arrange
        String ruleName = "도서 구매 적립";
        PointSaveRuleInfoResponse mockResponse = new PointSaveRuleInfoResponse(1L, ruleName, 200, "VARIABLE");
        when(pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName(ruleName))
                .thenReturn(new ResponseEntity<>(mockResponse, HttpStatus.OK));

        // Act
        PointSaveRuleInfoResponse result = pointSaveRuleCustomerService.getBookPointSaveRule();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getPointSaveRuleName()).isEqualTo(ruleName);
        assertThat(result.getPointSaveAmount()).isEqualTo(200);
        verify(pointSaveRuleCustomerAdapter, times(1)).getPointSaveRuleByRuleName(ruleName);
    }

    @Test
    void getBookPointSaveRule_FailedWithException() {
        // Arrange
        String ruleName = "도서 구매 적립";
        when(pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName(ruleName))
                .thenThrow(new RuntimeException("Connection Error"));

        // Act & Assert
        assertThatThrownBy(() -> pointSaveRuleCustomerService.getBookPointSaveRule())
                .isInstanceOf(PointSaveRuleGetFailedException.class)
                .hasMessage("책포인트 적립 정책 조회에 실패하였습니다.");
        verify(pointSaveRuleCustomerAdapter, times(1)).getPointSaveRuleByRuleName(ruleName);
    }

    @Test
    void getBookPointSaveRule_FailedWithNon2xxStatus() {
        // Arrange
        String ruleName = "도서 구매 적립";
        when(pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName(ruleName))
                .thenReturn(new ResponseEntity<>(HttpStatus.BAD_REQUEST)); // 400 응답을 반환

        // Act & Assert
        assertThatThrownBy(() -> pointSaveRuleCustomerService.getBookPointSaveRule())
                .isInstanceOf(PointSaveRuleGetFailedException.class)
                .hasMessage("책포인트 적립 정책 조회에 실패하였습니다.");

        // Verify
        verify(pointSaveRuleCustomerAdapter, times(1)).getPointSaveRuleByRuleName(ruleName);
    }

}
