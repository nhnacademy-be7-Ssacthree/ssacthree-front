package com.nhnacademy.mini_dooray.ssacthree_front.admin.service;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;

import java.util.List;

public interface AdminService {
    MessageResponse createDeliveryRule(DeliveryRuleCreateRequest deliveryRuleCreateRequest);

    List<DeliveryRuleGetResponse> getAllDeliveryRules();

    MessageResponse updateDeliveryRule(DeliveryRuleUpdateRequest deliveryRuleUpdateRequest);
}
