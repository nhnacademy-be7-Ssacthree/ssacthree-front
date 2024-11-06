package com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="gateway-service", url = "${admin-client.url}", contextId = "deliveryRuleClient")
public interface DeliveryRuleAdapter {
    @GetMapping("/delivery-rules")
    ResponseEntity<List<DeliveryRuleGetResponse>> getAllDeliveryRules();

    @PutMapping("/delivery-rules")
    ResponseEntity<MessageResponse> updateDeliveryRule(@RequestBody DeliveryRuleUpdateRequest deliveryRuleUpdateRequest);

    @PostMapping("/delivery-rules")
    ResponseEntity<MessageResponse> createDeliveryRule(@RequestBody DeliveryRuleCreateRequest deliveryRuleCreateRequest);
}
