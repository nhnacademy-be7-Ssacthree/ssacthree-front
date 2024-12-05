package com.nhnacademy.mini_dooray.ssacthree_front.order.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderUtil {
    public static String generateOrderNumber() {
        // 오늘의 날짜 (YYYYMMDD 형식)
        String today = LocalDate.now().toString().replace("-", "");

        // UUID 생성 (하이픈을 제외한 값)
        String uuid = UUID.randomUUID().toString().replace("-", "").toUpperCase();

        // 주문 번호 (년월일 + UUID 앞 12자리)
        String orderNumber = today + '-' + uuid.substring(0, 8); // UUID에서 앞 12자리 사용

        // 20글자 제한 (자르기)
        if (orderNumber.length() > 20) {
            orderNumber = orderNumber.substring(0, 20);
        }

        return orderNumber;
    }

    public static final ObjectMapper objectMapper = new ObjectMapper();


    public static String extractErrorMessage(FeignException ex) {
        // 기본 메시지 설정
        String errorMessage = "주문 조회 중 오류가 발생했습니다. 다시 시도해주세요.";

        Optional<ByteBuffer> responseBody = ex.responseBody();

        if (responseBody.isPresent()) {
            ByteBuffer buffer = responseBody.get();
            // ByteBuffer를 UTF-8로 디코딩하여 JSON 문자열로 변환
            String jsonResponse = StandardCharsets.UTF_8.decode(buffer).toString();

            try {
                // JSON 파싱
                Map<String, Object> errorMap = objectMapper.readValue(jsonResponse, Map.class);
                // "message" 필드를 추출
                if (errorMap.containsKey("message")) {
                    errorMessage = (String) errorMap.get("message");
                }
            } catch (Exception e) {
                // 내부적으로 로그를 남겨줌 (에러 페이지는 필요 없을듯)
                log.error("extractErrorMessage 변환 중 Feign exception response body의 에러를 가져오는데 실패: {}", jsonResponse, e);

                // 기본 메세지 출력
                errorMessage = "주문 조회 중 오류가 발생했습니다. 다시 시도해주세요.";
            }
        }

        return errorMessage;
    }
}
