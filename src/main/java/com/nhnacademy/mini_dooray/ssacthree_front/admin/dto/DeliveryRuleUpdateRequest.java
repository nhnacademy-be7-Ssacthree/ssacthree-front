package com.nhnacademy.mini_dooray.ssacthree_front.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeliveryRuleUpdateRequest {

    @NotNull
    @Setter
    private long deliveryRuleId;
}