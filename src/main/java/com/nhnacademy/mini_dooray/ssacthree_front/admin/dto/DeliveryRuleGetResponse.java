package com.nhnacademy.mini_dooray.ssacthree_front.admin.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class DeliveryRuleGetResponse {

    private long deliveryRuleId;
    private String deliveryRuleName;
    private long deliveryFee;
    private long deliveryDiscountCost;
    private boolean deliveryRuleIsSelected;
    private LocalDateTime deliveryRuleCreatedAt;
}
