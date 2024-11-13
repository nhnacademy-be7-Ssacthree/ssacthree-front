package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;

import java.util.List;

public interface PointSaveRuleService {
    List<PointSaveRuleGetResponse> getAllPointSaveRules();

    MessageResponse createPointSaveRule(PointSaveRuleCreateRequest pointSaveRuleCreateRequest);

    MessageResponse updatePointSaveRule(PointSaveRuleUpdateRequest pointSaveRuleUpdateRequest);
}
