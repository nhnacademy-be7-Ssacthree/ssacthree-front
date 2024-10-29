package com.nhnacademy.mini_dooray.ssacthree_front.cart.service;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate; // RedisTemplate<String, Object>로 변경
    private static final long CART_EXPIRATION_HOURS = 3;

    // 장바구니 ID로 장바구니 항목 가져오기
    public List<CartItem> getCartItemsByCartId(String cartId) {
        Object cartItems = redisTemplate.opsForValue().get(cartId);
        return cartItems instanceof List ? (List<CartItem>) cartItems : new ArrayList<>(); // 캐스팅을 통해 안전하게 반환, 없으면 빈 리스트 반환
    }

    // 빈 장바구니 초기화 및 cartId 관리
    public List<CartItem> initializeCart(HttpSession session) {
        String cartId = (String) session.getAttribute("cartId");

        // 세션에 cartId가 없으면 새로 생성
        if (cartId == null) {
            cartId = generateCartId(); // 새로운 cartId 생성
            session.setAttribute("cartId", cartId); // 세션에 cartId 저장
            // 빈 장바구니 생성 후 특정 물품 추가
            List<CartItem> cartItems = createEmptyCart(cartId); // 빈 장바구니 생성
            addDefaultItems(cartItems); // 기본 물품 추가
            saveNewCart(cartId, cartItems);
            return cartItems; // 장바구니 반환
        } else {
            return getCartItemsByCartId(cartId); // 기존 cartId로 장바구니 항목 가져오기
        }
    }

    // 장바구니 생성
    public List<CartItem> createEmptyCart(String cartId) {
        return new ArrayList<>();
    }

    // 장바구니 저장 및 만료 시간 설정
    public void saveCart(HttpSession session, List<CartItem> cartItems) {
        String cartId = (String) session.getAttribute("cartId");

        // cartId가 없으면 새로 생성
        if (cartId == null) {
            cartId = generateCartId(); // 새로운 cartId 생성
            session.setAttribute("cartId", cartId); // 세션에 cartId 저장
        }

        redisTemplate.opsForValue().set(cartId, cartItems, CART_EXPIRATION_HOURS, TimeUnit.HOURS); // Redis에 저장
    }

    public void saveNewCart(String cartId,List<CartItem> cartItems) {
        redisTemplate.opsForValue().set(cartId, cartItems, CART_EXPIRATION_HOURS, TimeUnit.HOURS);
    }

    // 총 가격 계산
    public int calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
            .mapToInt(item -> (int) (item.getPrice() * item.getQuantity()))
            .sum();
    }

    private String generateCartId() {
        return "guestCart:" + System.currentTimeMillis();
    }

    // 장바구니에 기본 물품 추가
    private void addDefaultItems(List<CartItem> cartItems) {
        // 예시로 물품을 추가
        cartItems.add(new CartItem(1, "책 제목 1", 1, 20000, null));
        cartItems.add(new CartItem(2, "책 제목 2", 1, 25000, null));
    }
}