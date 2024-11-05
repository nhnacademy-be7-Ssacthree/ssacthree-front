package com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.dto;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.domain.PointSaveType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PointSaveRuleGetResponse {

    private long pointSaveRuleId;
    private String pointSaveRuleName;
    private int pointSaveAmount;
    private LocalDateTime pointSaveRuleGenerateDate;
    private boolean pointSaveRuleIsSelected;
    private PointSaveType pointSaveType;
}
