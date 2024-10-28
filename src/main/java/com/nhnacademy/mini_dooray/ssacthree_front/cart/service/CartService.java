package com.nhnacademy.mini_dooray.ssacthree_front.cart.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.Cart;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.repo.CartRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {

    private final CartRepository cartRepository;

    private final RedisTemplate<String, CartItem> redisTemplate;

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String CART_KEY ="cart";

    // 장바구니에 항목 추가
    public void addItemToCart(long userId, CartItem cartItem) {
        String redisKey = CART_KEY + ":" + userId;
        redisTemplate.opsForHash().put(redisKey, cartItem.getId(), cartItem);
        log.info("Added item to cart for userId: {}", userId);
    }

    // 장바구니 항목 조회 (userId로 장바구니 항목 전체 조회)
    public List<CartItem> getCartItemsByUserId(long userId) {
        String redisKey = CART_KEY + ":" + userId;
        List<Object> cartItems = redisTemplate.opsForHash().values(redisKey);
        log.info("Retrieved cart items for userId: {}", userId);

        // Object 리스트를 CartItem 리스트로 변환
        return cartItems.stream()
            .filter(item -> item instanceof CartItem)  // 안전하게 타입 확인
            .map(item -> (CartItem) item)               // CartItem으로 변환
            .collect(Collectors.toList());
    }

    // 장바구니 항목 수정
    public void updateItemInCart(long userId, CartItem cartItem) {
        String redisKey = CART_KEY + ":" + userId;
        if (redisTemplate.opsForHash().hasKey(redisKey, cartItem.getId())) {
            redisTemplate.opsForHash().put(redisKey, cartItem.getId(), cartItem);
            log.info("Updated item in cart for userId: {}", userId);
        } else {
            log.warn("Item not found in cart for userId: {}", userId);
        }
    }

    // 장바구니 항목 삭제
    public void removeItemFromCart(long userId, long itemId) {
        String redisKey = CART_KEY + ":" + userId;
        redisTemplate.opsForHash().delete(redisKey, itemId);
        log.info("Removed item from cart for userId: {}", userId);
    }
}