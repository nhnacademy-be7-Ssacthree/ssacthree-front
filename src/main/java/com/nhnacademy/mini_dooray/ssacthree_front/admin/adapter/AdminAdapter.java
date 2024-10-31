package com.nhnacademy.mini_dooray.ssacthree_front.admin.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name="adminSendClient", url = "${admin-client.url}")
public interface AdminAdapter {

    @GetMapping("/deliveryRules")
    ResponseEntity<List<DeliveryRuleGetResponse>> getAllDeliveryRules();

    @PostMapping("/deliveryRules/create")
    ResponseEntity<MessageResponse> createDeliveryRule(@RequestBody DeliveryRuleCreateRequest deliveryRuleCreateRequest);
}
