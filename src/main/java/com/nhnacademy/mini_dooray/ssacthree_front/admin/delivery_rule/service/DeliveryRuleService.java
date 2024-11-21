package com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;

import java.util.List;

public interface DeliveryRuleService {
    MessageResponse createDeliveryRule(DeliveryRuleCreateRequest deliveryRuleCreateRequest);

    List<DeliveryRuleGetResponse> getAllDeliveryRules();

    MessageResponse updateDeliveryRule(DeliveryRuleUpdateRequest deliveryRuleUpdateRequest);

    DeliveryRuleGetResponse getCurrentDeliveryRule();

}
