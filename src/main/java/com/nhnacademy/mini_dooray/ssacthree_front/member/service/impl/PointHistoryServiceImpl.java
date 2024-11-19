package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PointHistoryGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.PointHistoryService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryServiceImpl implements PointHistoryService {

    private final MemberAdapter memberAdapter;


    @Override
    public Page<PointHistoryGetResponse> getPointHistory(
        Integer page, Integer size, String sort, String direction) {

        try {
            ResponseEntity<Page<PointHistoryGetResponse>> response = memberAdapter.getPointHistories(
                page, size, sort, direction);
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new RuntimeException("API 호출 실패: " + response.getStatusCode());

        } catch (FeignException e) {
            throw new RuntimeException("API 호출 중 예회 발생", e);
        }
    }
}
