package com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleGetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "deliveryRuleClient")
public interface DeliveryRuleCustomerAdapter {
    @GetMapping("/shop/delivery-rules/current")
    ResponseEntity<DeliveryRuleGetResponse> getCurrentDeliveryRule();
}
