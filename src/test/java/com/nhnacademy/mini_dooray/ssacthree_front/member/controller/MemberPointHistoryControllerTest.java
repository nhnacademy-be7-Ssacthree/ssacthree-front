package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PointHistoryGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.PointHistoryService;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberPointHistoryController.class)
class MemberPointHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PointHistoryService pointHistoryService;

    @MockBean
    private MemberService memberService;

    @Test
    void pointHistory() throws Exception {
        // Given
        int page = 0;
        int size = 5;
        String sort = "pointChangeDate";
        String direction = "DESC";

        PointHistoryGetResponse historyResponse = new PointHistoryGetResponse(100, null,
            "Purchase");
        Page<PointHistoryGetResponse> mockPage = new PageImpl<>(
            Collections.singletonList(historyResponse));

        when(pointHistoryService.getPointHistory(page, size, sort, direction))
            .thenReturn(mockPage);

        MemberInfoResponse memberInfoResponse = new MemberInfoResponse(
            1L, "user123", "John Doe", "123456789", "johndoe@example.com",
            "1990-01-01", 1000L, "Gold", 1.5f
        );

        when(memberService.getMemberInfo(any()))
            .thenReturn(memberInfoResponse);

        // When & Then
        mockMvc.perform(get("/point-histories")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort)
                .param("direction", direction))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("member"))
            .andExpect(model().attribute("member", memberInfoResponse))
            .andExpect(model().attributeExists("pointHistories"))
            .andExpect(model().attribute("pointHistories", mockPage))
            .andExpect(view().name("memberPointHistory"));
    }
}