package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PointHistoryGetResponse;
import feign.FeignException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PointHistoryServiceImplTest {

    @Mock
    private MemberAdapter memberAdapter;

    @InjectMocks
    private PointHistoryServiceImpl pointHistoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPointHistory_success() {
        // Given
        int page = 0;
        int size = 10;
        String sort = "pointAmount";
        String direction = "ASC";

        PointHistoryGetResponse responseDto = new PointHistoryGetResponse(100, null, "Purchase");
        Page<PointHistoryGetResponse> mockPage = new PageImpl<>(
            Collections.singletonList(responseDto));

        ResponseEntity<Page<PointHistoryGetResponse>> mockResponse =
            new ResponseEntity<>(mockPage, HttpStatus.OK);

        when(memberAdapter.getPointHistories(page, size, sort, direction))
            .thenReturn(mockResponse);

        // When
        Page<PointHistoryGetResponse> result =
            pointHistoryService.getPointHistory(page, size, sort, direction);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getPointAmount()).isEqualTo(100);
        verify(memberAdapter).getPointHistories(page, size, sort, direction);
    }

    @Test
    void getPointHistory_apiCallFails() {
        // Given
        int page = 0;
        int size = 10;
        String sort = "pointAmount";
        String direction = "ASC";

        ResponseEntity<Page<PointHistoryGetResponse>> mockResponse =
            new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        when(memberAdapter.getPointHistories(page, size, sort, direction))
            .thenReturn(mockResponse);

        // When & Then
        assertThatThrownBy(() -> pointHistoryService.getPointHistory(page, size, sort, direction))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("API 호출 실패");
        verify(memberAdapter).getPointHistories(page, size, sort, direction);
    }

    @Test
    void getPointHistory_feignException() {
        // Given
        int page = 0;
        int size = 10;
        String sort = "pointAmount";
        String direction = "ASC";

        // Mock FeignException
        FeignException feignException = mock(FeignException.class);

        when(memberAdapter.getPointHistories(page, size, sort, direction))
            .thenThrow(feignException);

        // When & Then
        assertThatThrownBy(() -> pointHistoryService.getPointHistory(page, size, sort, direction))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("API 호출 중 예회 발생");

        verify(memberAdapter).getPointHistories(page, size, sort, direction);
    }
}