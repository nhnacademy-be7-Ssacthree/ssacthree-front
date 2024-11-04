package com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DeliveryRuleUpdateRequest {

    @NotNull
    private long deliveryRuleId;
}