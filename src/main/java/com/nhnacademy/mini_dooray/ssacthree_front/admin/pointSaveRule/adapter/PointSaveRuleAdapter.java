package com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.adapter;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.dto.PointSaveRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="gateway-service", url = "${admin-client.url}", contextId = "pointSaveRuleClient")
public interface PointSaveRuleAdapter {
    @GetMapping("/pointSaveRules")
    ResponseEntity<List<PointSaveRuleGetResponse>> getAllPointSaveRules();

    @PutMapping("/pointSaveRules")
    ResponseEntity<MessageResponse> updatePointSaveRule(@RequestBody PointSaveRuleUpdateRequest pointSaveRuleUpdateRequest);

    @PostMapping("/pointSaveRules")
    ResponseEntity<MessageResponse> createPointSaveRule(@RequestBody PointSaveRuleCreateRequest pointSaveRuleCreateRequest);
}
