package com.nhnacademy.mini_dooray.ssacthree_front.order.utils;

import java.time.LocalDate;
import java.util.UUID;

public class OrderUtil {
    public static String generateOrderNumber() {
        // 오늘의 날짜 (YYYYMMDD 형식)
        String today = LocalDate.now().toString().replace("-", "");

        // UUID 생성 (하이픈을 제외한 값)
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // 주문 번호 (년월일 + UUID 앞 12자리)
        String orderNumber = today + uuid.substring(0, 12); // UUID에서 앞 12자리 사용

        // 20글자 제한 (자르기)
        if (orderNumber.length() > 20) {
            orderNumber = orderNumber.substring(0, 20);
        }

        return orderNumber;
    }
}
