package com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.adapter.DeliveryRuleAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.dto.DeliveryRuleUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.exception.DeliveryRuleCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.deliveryRule.service.DeliveryRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryRuleServiceImpl implements DeliveryRuleService {

    private final DeliveryRuleAdapter deliveryRUleAdapter;

    @Override
    public MessageResponse createDeliveryRule(DeliveryRuleCreateRequest deliveryRuleCreateRequest) {
        ResponseEntity<MessageResponse> response = deliveryRUleAdapter.createDeliveryRule(deliveryRuleCreateRequest);

        try {
            if(response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new DeliveryRuleCreateFailedException("배송정책 생성에 실패하였습니다.");
        }
        catch (HttpClientErrorException | HttpServerErrorException e ) {
            throw new DeliveryRuleCreateFailedException("배송정책 생성에 실패하였습니다.");
        }
    }

    @Override
    public List<DeliveryRuleGetResponse> getAllDeliveryRules() {
        ResponseEntity<List<DeliveryRuleGetResponse>> response = deliveryRUleAdapter.getAllDeliveryRules();

        try {
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new DeliveryRuleCreateFailedException("배송정책 조회에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e ) {
            throw new DeliveryRuleCreateFailedException("배송정책 조회에 실패하였습니다.");
        }
    }

    @Override
    public MessageResponse updateDeliveryRule(DeliveryRuleUpdateRequest deliveryRuleUpdateRequest) {
        ResponseEntity<MessageResponse> response = deliveryRUleAdapter.updateDeliveryRule(deliveryRuleUpdateRequest);

        try {
            if(response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new DeliveryRuleCreateFailedException("배송정책 수정에 실패하였습니다.");
        }
        catch (HttpClientErrorException | HttpServerErrorException e ) {
            throw new DeliveryRuleCreateFailedException("배송정책 수정에 실패하였습니다.");
        }
    }
}
