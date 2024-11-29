package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.adapter.PointSaveRuleCustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.PointSaveRuleCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointSaveRuleCustomerServiceImpl implements PointSaveRuleCustomerService {

    private final PointSaveRuleCustomerAdapter pointSaveRuleCustomerAdapter;

    @Override
    public PointSaveRuleInfoResponse getPointSaveRuleByRuleName(String pointSaveRuleName) {
        try {
            ResponseEntity<PointSaveRuleInfoResponse> responseEntity = pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName(pointSaveRuleName);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                // 필요한 에러 처리 로직 추가
                throw new RuntimeException("API 호출 실패: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            // 예외 로깅 및 처리
            throw new RuntimeException("API 호출 중 예외 발생", e);
        }
    }

    @Override
    public PointSaveRuleInfoResponse getBookPointSaveRule() {
        try {
            ResponseEntity<PointSaveRuleInfoResponse> responseEntity = pointSaveRuleCustomerAdapter.getPointSaveRuleByRuleName("도서 구매 적립");
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                // 필요한 에러 처리 로직 추가
                throw new RuntimeException("API 호출 실패: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            // 예외 로깅 및 처리
            throw new RuntimeException("API 호출 중 예외 발생", e);
        }
    }
}
