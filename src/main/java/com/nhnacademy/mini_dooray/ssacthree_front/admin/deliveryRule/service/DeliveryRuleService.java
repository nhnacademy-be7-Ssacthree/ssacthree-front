package com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;

import java.util.List;

public interface DeliveryRuleService {
    MessageResponse createDeliveryRule(DeliveryRuleCreateRequest deliveryRuleCreateRequest);

    List<DeliveryRuleGetResponse> getAllDeliveryRules();

    MessageResponse updateDeliveryRule(DeliveryRuleUpdateRequest deliveryRuleUpdateRequest);
}
