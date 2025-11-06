package com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.adapter.DeliveryRuleAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.adapter.DeliveryRuleCustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.exception.DeliveryRuleCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.exception.DeliveryRuleGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.exception.DeliveryRuleUpdateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service.DeliveryRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.ResponseEntityHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryRuleServiceImpl implements DeliveryRuleService {

    private final DeliveryRuleAdapter deliveryRuleAdapter;
    private final DeliveryRuleCustomerAdapter deliveryRuleCustomerAdapter;

    private static final String FAILED_TO_DELIVERY = "배송정책 생성에 실패하였습니다.";

    @Override
    public MessageResponse createDeliveryRule(DeliveryRuleCreateRequest deliveryRuleCreateRequest) {
        return ResponseEntityHandler.getResponseBody(
            deliveryRuleAdapter.createDeliveryRule(deliveryRuleCreateRequest),
            () -> new DeliveryRuleCreateFailedException(FAILED_TO_DELIVERY)
        );
    }

    @Override
    public List<DeliveryRuleGetResponse> getAllDeliveryRules() {
        return ResponseEntityHandler.getResponseBody(
            deliveryRuleAdapter.getAllDeliveryRules(),
            () -> new DeliveryRuleGetFailedException(FAILED_TO_DELIVERY)
        );
    }

    @Override
    public MessageResponse updateDeliveryRule(DeliveryRuleUpdateRequest deliveryRuleUpdateRequest) {
        return ResponseEntityHandler.getResponseBody(
            deliveryRuleAdapter.updateDeliveryRule(deliveryRuleUpdateRequest),
            () -> new DeliveryRuleUpdateFailedException("배송정책 수정에 실패하였습니다.")
        );
    }

    @Override
    public DeliveryRuleGetResponse getCurrentDeliveryRule() {
        return ResponseEntityHandler.getResponseBody(
            deliveryRuleCustomerAdapter.getCurrentDeliveryRule(),
            () -> new DeliveryRuleGetFailedException("배송정책 조회에 실패하였습니다.")
        );
    }
}
