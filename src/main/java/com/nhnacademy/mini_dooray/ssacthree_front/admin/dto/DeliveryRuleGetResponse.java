package com.nhnacademy.mini_dooray.ssacthree_front.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class DeliveryRuleGetResponse {

    private long deliveryRuleId;
    private String deliveryRuleName;
    private int deliveryFee;
    private int deliveryDiscountCost;
    private boolean deliveryRuleIsSelected;
    private LocalDateTime deliveryRuleCreatedAt;
}
