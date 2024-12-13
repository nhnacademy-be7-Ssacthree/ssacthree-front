package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.adapter.PointSaveRuleCustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.exception.PointSaveRuleGetFailedException;
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
                throw new PointSaveRuleGetFailedException("적립정책의 이름으로 포인트 적립 정책 조회에 실패하였습니다.");
            }
        } catch (Exception e) {
            // 예외 로깅 및 처리
            throw new PointSaveRuleGetFailedException("적립정책의 이름으로 포인트 적립 정책 조회에 실패하였습니다.");
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
                throw new PointSaveRuleGetFailedException("책포인트 적립 정책 조회에 실패하였습니다.");
            }
        } catch (Exception e) {
            // 예외 로깅 및 처리
            throw new PointSaveRuleGetFailedException("책포인트 적립 정책 조회에 실패하였습니다.");
        }
    }
}
