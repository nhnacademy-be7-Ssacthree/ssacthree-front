package com.nhnacademy.mini_dooray.ssacthree_front.payment.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.adapter.PaymentAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.dto.PaymentRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentAdapter paymentAdapter;

    // TODO : 결제 정보 저장하기, api요청
    public MessageResponse savePayment(PaymentRequest paymentRequest) {
        ResponseEntity<MessageResponse> response = paymentAdapter.savePayment(paymentRequest);
        return response.getBody();
    }

    @Override
    public ResponseEntity<String> cancelPayment(String paymentKey, String cancelReason) {
        // API URL 생성
        String url = "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel";

        // 요청 헤더 설정
        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizations);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 본문 설정
        Map<String, String> body = new HashMap<>();
        body.put("cancelReason", cancelReason);

        // HttpEntity 생성
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        // RestTemplate 사용
        RestTemplate restTemplate = new RestTemplate();
        try {
            return restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        } catch (HttpClientErrorException.Forbidden ex) {
            // 403 오류가 발생한 경우, 추가적인 로깅이나 처리가 필요
            System.out.println("API 요청이 거부되었습니다: " + ex.getResponseBodyAsString());
            throw ex;
        }
    }
}
