package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.adapter.PointSaveRuleAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.exception.PointSaveRuleCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service.PointSaveRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.exception.PointSaveRuleGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.exception.PointSaveRuleUpdateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.ResponseEntityHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointSaveRuleServiceImpl implements PointSaveRuleService {

    private final PointSaveRuleAdapter pointSaveRuleAdapter;

    @Override
    public List<PointSaveRuleGetResponse> getAllPointSaveRules() {
        return ResponseEntityHandler.getResponseBody(
            pointSaveRuleAdapter.getAllPointSaveRules(),
            () -> new PointSaveRuleGetFailedException("포인트 적립 정책 조회에 실패하였습니다.")
        );
    }

    @Override
    public MessageResponse createPointSaveRule(PointSaveRuleCreateRequest pointSaveRuleCreateRequest) {
        return ResponseEntityHandler.getResponseBody(
            pointSaveRuleAdapter.createPointSaveRule(pointSaveRuleCreateRequest),
            () -> new PointSaveRuleCreateFailedException("포인트 적립 정책 생성에 실패하였습니다.")
        );
    }

    @Override
    public MessageResponse updatePointSaveRule(PointSaveRuleUpdateRequest pointSaveRuleUpdateRequest) {
        return ResponseEntityHandler.getResponseBody(
            pointSaveRuleAdapter.updatePointSaveRule(pointSaveRuleUpdateRequest),
            () -> new PointSaveRuleUpdateFailedException("포인트 적립 정책 수정에 실패하였습니다.")
        );
    }
}
