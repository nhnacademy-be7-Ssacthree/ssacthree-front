package com.nhnacademy.mini_dooray.ssacthree_front.cart.service;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 장바구니에 필요한 service
 *
 * @author : 조병현
 * @version : 2024/10/30 0.1.0
 */
@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate; // RedisTemplate<String, Object>로 변경
    private static final long CART_EXPIRATION_HOURS = 3;

    /**
     *
     * @param cartId 카트 번호
     * @return List<CartItem> cartItems
     * 장바구니 ID로 장바구니 항목 가져오기
     */
    public List<CartItem> getCartItemsByCartId(String cartId) {
        Object cartItems = redisTemplate.opsForValue().get(cartId);
        return cartItems instanceof List ? (List<CartItem>) cartItems : new ArrayList<>(); // 캐스팅을 통해 안전하게 반환, 없으면 빈 리스트 반환
    }

    /**
     *
     * @param session 세션
     * @return cartItems
     * 빈 장바구니 초기화 및 cartId 관리
     */
    public List<CartItem> initializeCart(HttpSession session) {
        String cartId = (String) session.getAttribute("cartId");

        // 세션에 cartId가 없으면 새로 생성
        if (cartId == null) {
            cartId = generateCartId(); // 새로운 cartId 생성
            session.setAttribute("cartId", cartId); // 세션에 cartId 저장
            // 빈 장바구니 생성 후 특정 물품 추가
            List<CartItem> cartItems = createEmptyCart(cartId); // 빈 장바구니 생성
            addDefaultItems(cartItems); // 기본 물품 추가(확인용 나중에 삭제 예정)
            saveNewCart(cartId, cartItems);
            return cartItems; // 장바구니 반환
        } else {
            return getCartItemsByCartId(cartId); // 기존 cartId로 장바구니 항목 가져오기
        }
    }

    /**
     *
     * @param cartId 카트 번호
     * @return List<CartItem> 빈 장바구니
     * // 장바구니 생성
     */
    public List<CartItem> createEmptyCart(String cartId) {
        return new ArrayList<>();
    }

    public void saveNewCart(String cartId,List<CartItem> cartItems) {
        redisTemplate.opsForValue().set(cartId, cartItems, CART_EXPIRATION_HOURS, TimeUnit.HOURS);
    }

    /**
     *
     * @param cartItems 장바구니에 담겨있는 도서들
     * @return 도서의 총 합
     * 총 가격 계산
     */
    public int calculateTotalPrice(List<CartItem> cartItems) {
        return cartItems.stream()
            .mapToInt(item -> (int) (item.getPrice() * item.getQuantity()))
            .sum();
    }

    /**
     *
     * @return "guestCart:12031053295"
     * 비회원 장바구니 id 생성
     */
    private String generateCartId() {
        return "guestCart:" + System.currentTimeMillis();
    }

    /**
     * GET localhost:/shop
     * @param cartItems 물품추가
     * 삭제예정
     */
    // 장바구니에 기본 물품 추가
    private void addDefaultItems(List<CartItem> cartItems) {
        // 예시로 물품을 추가 (나중에 삭제 예정)
        cartItems.add(new CartItem(1, "책 제목 1", 1, 20000, null));
        cartItems.add(new CartItem(2, "책 제목 2", 1, 25000, null));
    }

    /**
     * PUT localhost:/shop/{itemId}
     * @param session 세션(비회원)
     * @param itemId 도서 아이디
     * @param quantityChange 도서 개수
     * 도서의 수량 업데이트
     */
    public void updateItemQuantity(HttpSession session, Long itemId, int quantityChange) { //수량 추가
        String cartId = (String) session.getAttribute("cartId");
        List<CartItem> cartItems = getCartItemsByCartId(cartId);

        // 수량이 변경된 새 CartItem 리스트 생성
        List<CartItem> updatedCartItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getId() == (itemId)) {
                int newQuantity = cartItem.getQuantity() + quantityChange;
                if (newQuantity > 0) { // 수량이 0보다 큰 경우에만 새 객체 추가
                    CartItem newCartItem = new CartItem(cartItem.getId(), cartItem.getTitle(), newQuantity, cartItem.getPrice(), cartItem.getImage());
                    updatedCartItems.add(newCartItem);
                }
            } else {
                updatedCartItems.add(cartItem); // 다른 항목은 그대로 추가
            }
        }

        // 변경된 장바구니 항목을 Redis에 저장
        redisTemplate.opsForValue().set(cartId, updatedCartItems, CART_EXPIRATION_HOURS, TimeUnit.HOURS);
    }

    /**
     * DELETE localhost/shop
     * @param session 세션(비회원)
     * @param itemId (도서 아이디)
     * 장바구니에 데이터를 삭제할 때 쓰는 서비스
     */
    public void deleteItem(HttpSession session, Long itemId) {
        // 세션에서 cartId 가져오기
        String cartId = (String) session.getAttribute("cartId");

        if (cartId != null) {
            // Redis에서 현재 장바구니 항목 가져오기
            List<CartItem> cartItems = getCartItemsByCartId(cartId);

            // Iterator를 사용하여 항목 제거
            Iterator<CartItem> iterator = cartItems.iterator();
            while (iterator.hasNext()) {
                CartItem cartItem = iterator.next();
                if (cartItem.getId() == (itemId)) {
                    iterator.remove(); // 항목 삭제
                    break; // 일치하는 항목을 찾으면 루프 종료
                }
            }

            // Redis에 변경된 장바구니 항목 저장
            redisTemplate.opsForValue().set(cartId, cartItems, CART_EXPIRATION_HOURS, TimeUnit.HOURS);
        }
    }

    /**
     * POST localhost:/shop/{itemId}
     * @param session 세션(비회원)
     * @param itemId 도서 아이디
     * @param title 도서 제목
     * @param price 도서 가격
     * @param image 도서 이미지
     * 장바구니에 새로운 책을 등록할 때 쓰는 서비스
     */
    public void addNewBook(HttpSession session, Long itemId, String title, int price, byte[] image) {
        String cartId = (String) session.getAttribute("cartId");
        List<CartItem> cartItems = getCartItemsByCartId(cartId);
        for (CartItem cartItem : cartItems) {
            if(cartItem.getId() == itemId) { //만약에 똑같은 도서 가져오면 개수만 늘려주기
                updateItemQuantity(session,itemId,1);
                return;
            }
        }

        cartItems.add(new CartItem(itemId, title, 1, price, image));

        redisTemplate.opsForValue().set(cartId, cartItems, CART_EXPIRATION_HOURS, TimeUnit.HOURS);
    }
}