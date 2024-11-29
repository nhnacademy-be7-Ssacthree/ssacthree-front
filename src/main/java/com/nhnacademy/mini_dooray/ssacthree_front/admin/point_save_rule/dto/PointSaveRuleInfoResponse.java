package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PointSaveRuleInfoResponse {
    private Long pointSaveRuleId;
    private String pointSaveRuleName;
    private int pointSaveAmount;
    private String pointSaveType;
}
