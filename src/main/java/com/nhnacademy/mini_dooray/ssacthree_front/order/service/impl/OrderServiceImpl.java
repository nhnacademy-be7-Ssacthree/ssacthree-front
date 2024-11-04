package com.nhnacademy.mini_dooray.ssacthree_front.order.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.GuestCartInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final RedisTemplate<String, Object> redisTemplate;

    // 쿠키에서 확인한 장바구니 id -> redis에서 찾아서 띄우기
//    @Override
//    public GuestCartInfoResponse createGusetOrderSheet(HttpSession session) {
//        String cartId = (String) session.getAttribute("cartId");
//        //cartId로 redis에서 찾기
//        Object cartItems = redisTemplate.opsForValue().get(cartId);
//
//        // 추후, id랑 quentity만 받도록 json 파싱 필요할 듯. 장바구니에서도 수정 필요 예상
//
//
//        return null;
//    }

    //임시로 일단 병현님이 만든 redis cart 형식 따라가기
    @Override
    public List<CartItem> createGusetOrderSheet(HttpSession session) {
        // 세션에 저장된 cartId찾기 -> 바로 이름을 CART_NO로 넣으면 안되나 ?
        String cartId = (String) session.getAttribute("cartId");
        // cartId로 redis에서 찾기
        Object cartItems = redisTemplate.opsForValue().get(cartId);

        // 추후, id랑 quentity만 받도록 json 파싱 필요할 듯. 장바구니에서도 수정 필요 예상

        return (List<CartItem>) cartItems;
    }
}
