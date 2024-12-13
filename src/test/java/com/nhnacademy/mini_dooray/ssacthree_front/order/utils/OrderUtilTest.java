package com.nhnacademy.mini_dooray.ssacthree_front.order.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
class OrderUtilTest {

  @Test
  void testGenerateOrderNumber() {
    String orderNumber = OrderUtil.generateOrderNumber();
    log.info("test ordernumber: {}", orderNumber);
    // 오늘의 날짜 확인
    String today = LocalDate.now().toString().replace("-", "");

    // 주문 번호 형식 확인
    assertTrue(orderNumber.startsWith(today), "Order number should start with today's date.");

    // 주문번호는 17글자
    assertEquals(17, orderNumber.length(), "Order number length should be 17 characters.");
  }

  @Test
  void testExtractErrorMessage_withResponseBody() {
    // Mocking FeignException
    String errorMessageJson = "{\"message\": \"Invalid order ID.\"}";
    ByteBuffer buffer = StandardCharsets.UTF_8.encode(errorMessageJson);

    FeignException mockException = mock(FeignException.class);
    when(mockException.responseBody()).thenReturn(Optional.of(buffer));

    // 테스트 실행
    String extractedMessage = OrderUtil.extractErrorMessage(mockException);

    // 결과 검증
    assertEquals("Invalid order ID.", extractedMessage, "The extracted message should match the JSON message.");
  }

  @Test
  void testExtractErrorMessage_withNoResponseBody() {
    // Mocking FeignException without response body
    FeignException mockException = mock(FeignException.class);
    when(mockException.responseBody()).thenReturn(Optional.empty());

    // 테스트 실행
    String extractedMessage = OrderUtil.extractErrorMessage(mockException);

    // 결과 검증
    assertEquals("주문 조회 중 오류가 발생했습니다. 다시 시도해주세요.", extractedMessage,
        "The extracted message should return the default message when no response body is present.");
  }

  @Test
  void testExtractErrorMessage_withMalformedJson() {
    // Mocking FeignException with invalid JSON
    String invalidJson = "{\"invalid_json";
    ByteBuffer buffer = StandardCharsets.UTF_8.encode(invalidJson);

    FeignException mockException = mock(FeignException.class);
    when(mockException.responseBody()).thenReturn(Optional.of(buffer));

    // 테스트 실행
    String extractedMessage = OrderUtil.extractErrorMessage(mockException);

    // 결과 검증
    assertEquals("주문 조회 중 오류가 발생했습니다. 다시 시도해주세요.", extractedMessage,
        "The extracted message should return the default message when the JSON is malformed.");
  }
}
