package com.nhnacademy.mini_dooray.ssacthree_front.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class DeliveryRuleCreateRequest {

    @NotBlank
    private String deliveryRuleName;

    @NotNull
    private int deliveryFee;

    private int deliveryDiscountCost;

    @NotNull
    private boolean deliveryRuleIsSelected;

    @NotNull
    private LocalDateTime deliveryRuleCreatedAt = LocalDateTime.now();
}
