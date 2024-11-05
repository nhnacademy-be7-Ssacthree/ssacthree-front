package com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;

import java.util.List;

public interface PointSaveRuleService {
    List<PointSaveRuleGetResponse> getAllPointSaveRules();

    MessageResponse createPointSaveRule(PointSaveRuleCreateRequest pointSaveRuleCreateRequest);
}