package com.nhnacademy.mini_dooray.ssacthree_front.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DeliveryRuleGetResponse {

    private long deliveryRuleId;
    private String deliveryRuleName;
    private long deliveryFee;
    private long deliveryDiscountCost;
    private boolean deliveryRuleIsSelected;
    private LocalDateTime deliveryRuleCreatedAt;
}
