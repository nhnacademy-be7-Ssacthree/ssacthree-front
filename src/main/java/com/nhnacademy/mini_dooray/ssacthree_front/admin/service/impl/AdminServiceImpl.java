package com.nhnacademy.mini_dooray.ssacthree_front.admin.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.adapter.AdminAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.DeliveryRuleCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.exception.DeliveryRuleCreateFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.service.AdminService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminAdapter adminAdapter;

    @Override
    public MessageResponse createDeliveryRule(DeliveryRuleCreateRequest request) {
        ResponseEntity<MessageResponse> response = adminAdapter.createDeliveryRule(request);

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
        ResponseEntity<List<DeliveryRuleGetResponse>> response = adminAdapter.getAllDeliveryRules();

        try {
            if(response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new DeliveryRuleCreateFailedException("배송정책 조회에 실패하였습니다.");
        }
        catch (HttpClientErrorException | HttpServerErrorException e ) {
            throw new DeliveryRuleCreateFailedException("배송정책 조회에 실패하였습니다.");
        }
    }
}
