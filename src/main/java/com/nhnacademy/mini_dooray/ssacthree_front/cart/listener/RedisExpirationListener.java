package com.nhnacademy.mini_dooray.ssacthree_front.cart.listener;


import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.controller.CartController;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisExpirationListener implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CartController cartController;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        if (expiredKey.startsWith("alert:")) {
            String originalCartId = expiredKey.substring(6); // 실제 장바구니 키 이름 추출
            Map<String, Object> cartData = (Map<String, Object>) redisTemplate.opsForValue().get(originalCartId);

            if (cartData != null) {
                try {
                    // cartItems와 accessToken 추출
                    Object cartItemsObj = cartData.get("cartItems");
                    Object customerIdObj = cartData.get("customerId");

                    if (cartItemsObj != null && customerIdObj instanceof Long customerId) {
                        // cartItems JSON을 List<CartItem>로 변환
                        List<CartItem> cartItems = (List<CartItem>) cartItemsObj;

                        // 데이터베이스에 저장
                        cartController.saveInDB(cartItems, customerId);

                        log.info("Access Token: {}", customerId);
                    } else {
                        log.warn("cartItems 또는 accessToken이 없습니다. 키: {}", expiredKey);
                    }
                } catch (IllegalArgumentException e) {
                    log.warn("데이터 변환 중 오류 발생, 키: {}", expiredKey, e);
                }
            } else {
                log.warn("Redis에서 키 [{}]에 대한 데이터가 없습니다.", expiredKey);
            }
        }
    }
}