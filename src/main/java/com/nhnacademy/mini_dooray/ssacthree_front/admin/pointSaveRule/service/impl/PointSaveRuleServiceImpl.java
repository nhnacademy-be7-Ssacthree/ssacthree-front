package com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.adapter.PointSaveRuleAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.dto.PointSaveRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.dto.PointSaveRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.dto.PointSaveRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.exception.PointSaveRuleCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.pointSaveRule.service.PointSaveRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointSaveRuleServiceImpl implements PointSaveRuleService {

    private final PointSaveRuleAdapter pointSaveRuleAdapter;

    @Override
    public List<PointSaveRuleGetResponse> getAllPointSaveRules() {
        ResponseEntity<List<PointSaveRuleGetResponse>> response = pointSaveRuleAdapter.getAllPointSaveRules();

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new PointSaveRuleCreateFailedException("포인트 적립 정책 조회에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PointSaveRuleCreateFailedException("포인트 적립 정책 조회에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse createPointSaveRule(PointSaveRuleCreateRequest pointSaveRuleCreateRequest) {
        ResponseEntity<MessageResponse> response = pointSaveRuleAdapter.createPointSaveRule(pointSaveRuleCreateRequest);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new PointSaveRuleCreateFailedException("포인트 적립 정책 생성에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PointSaveRuleCreateFailedException("포인트 적립 정책 생성에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse updatePointSaveRule(PointSaveRuleUpdateRequest pointSaveRuleUpdateRequest) {
        ResponseEntity<MessageResponse> response = pointSaveRuleAdapter.updatePointSaveRule(pointSaveRuleUpdateRequest);

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new PointSaveRuleCreateFailedException("포인트 적립 정책 수정에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new PointSaveRuleCreateFailedException("포인트 적립 정책 수정에 실패하였습니다.");
        }
    }
}
