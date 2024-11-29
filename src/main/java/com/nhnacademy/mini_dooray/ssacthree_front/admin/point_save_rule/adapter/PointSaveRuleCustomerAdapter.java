package com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.point_save_rule.dto.PointSaveRuleInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "gateway-service", url = "${member.url}", contextId = "pointRuleCustomerClient")
public interface PointSaveRuleCustomerAdapter {
    @GetMapping("/shop/point-save-rules/{point-save-rule-name}")
    ResponseEntity<PointSaveRuleInfoResponse> getPointSaveRuleByRuleName(@PathVariable(name = "point-save-rule-name") String ruleName);
}
