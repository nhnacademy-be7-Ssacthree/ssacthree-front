package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleInfoResponse;

public interface PointSaveRuleCustomerService {

    PointSaveRuleInfoResponse getPointSaveRuleByRuleName(String pointSaveRuleName);

    

    PointSaveRuleInfoResponse getBookPointSaveRule();
}
