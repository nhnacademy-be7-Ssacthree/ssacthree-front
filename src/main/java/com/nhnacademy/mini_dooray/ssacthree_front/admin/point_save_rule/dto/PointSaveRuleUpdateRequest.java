package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PointSaveRuleUpdateRequest {

    @NotNull
    private Long pointSaveRuleId;
}
