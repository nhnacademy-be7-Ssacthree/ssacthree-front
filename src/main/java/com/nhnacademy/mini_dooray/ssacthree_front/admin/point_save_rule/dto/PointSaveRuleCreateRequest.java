package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.domain.PointSaveType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PointSaveRuleCreateRequest {

    @NotBlank
    private String pointSaveRuleName;

    @NotNull
    @Min(0)
    private int pointSaveAmount;

    @NotNull
    private PointSaveType pointSaveType;
}
