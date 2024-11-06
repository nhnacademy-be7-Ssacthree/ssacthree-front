package com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.dto;

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
