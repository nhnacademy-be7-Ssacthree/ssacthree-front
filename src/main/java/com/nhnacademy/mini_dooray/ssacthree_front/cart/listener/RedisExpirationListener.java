package com.nhnacademy.mini_dooray.ssacthree_front.cart.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import java.util.List;
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
    private final ObjectMapper objectMapper;
    private final CartService cartService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        if (expiredKey.startsWith("alert:memberCart:")) {
            String originalCartId = expiredKey.substring(6); // 실제 장바구니 키 이름 추출
            String cartData = (String) redisTemplate.opsForValue().get(originalCartId);

            if (cartData != null) {
                try {
                    // JSON 문자열을 파싱하여 CartItem 리스트로 변환
                    JsonNode rootNode = objectMapper.readTree(cartData);
                    JsonNode cartItemsNode = rootNode.get(1); // 실제 CartItem 데이터가 포함된 배열

                    List<CartItem> cartItems = objectMapper.readValue(cartItemsNode.toString(), new TypeReference<List<CartItem>>() {});

                    // 각 CartItem의 ID와 수량을 추출하여 데이터베이스에 저장
                    for (CartItem cartItem : cartItems) {
                        saveItemById(cartItem.getId(), cartItem.getQuantity());
                    }

                } catch (JsonProcessingException e) {
                    log.warn("Redis에서 키 [{}]에 대한 데이터가 없습니다.",expiredKey);
                }
            }
        }
    }

    public void saveItemById(Long itemId, int quantity) {
        // cartService에서 itemId와 quantity를 사용하여 저장
        cartService.saveCartInDB(itemId, quantity);
    }
}