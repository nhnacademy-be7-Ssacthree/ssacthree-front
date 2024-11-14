package com.nhnacademy.mini_dooray.ssacthree_front.cart.service;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.adapter.CartAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.dto.CartRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.AddressFailedException;
import feign.FeignException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

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
    private final CartAdapter cartAdapter;

    static final long CART_EXPIRATION_MINUTES = 30;
    private static final String BEARER = "Bearer ";

    /**
     *
     * @param cartId 카트 번호
     * @return List<CartItem> cartItems
     * 장바구니 ID로 장바구니 항목 가져오기
     */
    public List<CartItem> getCartItemsByCartId(String cartId) {
        Object cartData = redisTemplate.opsForValue().get(cartId);

        if (cartData instanceof Map) {
            Map<String, Object> cartDataMap = (Map<String, Object>) cartData;
            Object cartItems = cartDataMap.get("cartItems");

            if (cartItems instanceof List) {
                return (List<CartItem>) cartItems;
            }
        }

        // cartItems가 없거나 데이터가 없는 경우 빈 리스트 반환
        return new ArrayList<>();
    }

    public Long getCustomerIdByCartId(String cartId) {
        Object cartData = redisTemplate.opsForValue().get(cartId);

        if (cartData instanceof Map) {
            Map<String, Object> cartDataMap = (Map<String, Object>) cartData;
            Object customerId = cartDataMap.get("customerId");

            if (customerId instanceof Long) {
                return (Long) customerId;
            }
        }

        // accessToken이 없거나 데이터가 없는 경우 빈 문자열 반환
        return null;
    }


    public List<CartItem> initializeCart(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            session = request.getSession(true);
            String cartId = session.getId();
            // 빈 장바구니 생성 후 특정 물품 추가
            List<CartItem> cartItems = new ArrayList<>(); // 빈 장바구니 생성
            saveCart(cartId,cartItems,null);
            return cartItems; // 장바구니 반환
        }
        String cartId = session.getId();
        return getCartItemsByCartId(cartId); // 기존 cartId로 장바구니 항목 가져오기
    }


    public void saveCart(String cartId,List<CartItem> cartItems, Long customerId) {

        // 여러 데이터를 저장할 수 있도록 Map 생성
        Map<String, Object> cartData = new HashMap<>();
        cartData.put("cartItems", cartItems);
        cartData.put("customerId", customerId);

        // `cartId` 키로 1시간 동안 Redis에 `cartData` 저장
        redisTemplate.opsForValue().set(cartId, cartData, CART_EXPIRATION_MINUTES, TimeUnit.MINUTES);

        // 경고 키 생성
        String alertKey = "alert:" + cartId;

        // 경고 키를 59분으로 설정하여 만료 1분 전에 알림 받기
        redisTemplate.opsForValue().set(alertKey, "expiring soon", 29, TimeUnit.MINUTES);
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


    public void updateItemQuantity(HttpServletRequest request, Long itemId, int quantityChange) {
        HttpSession session = request.getSession();
        String cartId = session.getId();
        List<CartItem> cartItems = getCartItemsByCartId(cartId);

        // 수량이 변경된 새 CartItem 리스트 생성
        List<CartItem> updatedCartItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            if (cartItem.getId() == (itemId)) {
                int newQuantity = cartItem.getQuantity() + quantityChange;
                if (newQuantity > 0) {
                    CartItem newCartItem = new CartItem(cartItem.getId(), cartItem.getTitle(), newQuantity, cartItem.getPrice(), cartItem.getBookThumbnailImageUrl());
                    updatedCartItems.add(newCartItem);
                }
            } else {
                updatedCartItems.add(cartItem);
            }
        }

        Long customerId = getCustomerIdByCartId(cartId);
        saveCart(cartId, updatedCartItems, customerId);
    }


    public void deleteItem(HttpServletRequest request, Long itemId) {
        HttpSession session = request.getSession();
        String cartId = session.getId();
        List<CartItem> cartItems = getCartItemsByCartId(cartId);

        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem cartItem = iterator.next();
            if (cartItem.getId() == (itemId)) {
                iterator.remove();
                break;
            }
        }

        Long customerId = getCustomerIdByCartId(cartId);
        saveCart(cartId, cartItems, customerId);
    }


    public void addNewBook(HttpServletRequest request, Long itemId, String title, int quantity, int price, String image) {
        HttpSession session = request.getSession();
        String cartId = session.getId();
        List<CartItem> cartItems = getCartItemsByCartId(cartId);

        for (CartItem cartItem : cartItems) {
            if (cartItem.getId() == (itemId)) {
                updateItemQuantity(request, itemId, quantity);
                return;
            }
        }

        cartItems.add(new CartItem(itemId, title, quantity, price, image));

        Long customerId = getCustomerIdByCartId(cartId);
        saveCart(cartId, cartItems, customerId);
    }





    public ResponseEntity<Void> saveCartInDB(List<CartItem> cartItems, Long customerId) {

        List<CartRequest> cartRequests = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            Long id = cartItem.getId();
            int quantity = cartItem.getQuantity();
            cartRequests.add(new CartRequest(id, quantity));
        }


        try{
            ResponseEntity<Void> response = cartAdapter.saveCartInDB(cartRequests, customerId);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new AddressFailedException("주소 삭제에 실패하였습니다.");
            }
            return response;
        }catch (FeignException e) {
            throw new RuntimeException("요청 오류: " + e.getMessage(), e);
        }
    }

    public CartItem getRandomBook(Long bookId, HttpServletRequest request) {
        String accessToken = getAccessToken(request);
        try{
            ResponseEntity<CartItem> response = cartAdapter.getRandomBook(
                BEARER + accessToken, bookId);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

        }catch (HttpClientErrorException | HttpServerErrorException e){
            throw new RuntimeException("요청 오류");
        }
        return null;
    }

    public void getMemberCart(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        String cartId = session.getId();
        String accessToken = getAccessToken(request);
        try {
            ResponseEntity<List<CartItem>> response = cartAdapter.getCartItems(BEARER + accessToken);

            if (response.getStatusCode().is2xxSuccessful()) {
                List<CartItem> cartItems = response.getBody();
                Long customerId = getLoginCustomerId(request);
                saveCart(cartId,cartItems,customerId);
            }

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("요청 오류");
        }
    }

    private Long getLoginCustomerId(HttpServletRequest request) {
        String accessToken = getAccessToken(request);
        try {
            ResponseEntity<Long> response = cartAdapter.getCustomerId(BEARER + accessToken);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new RuntimeException("아이디를 가져올 수 없습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new RuntimeException("요청 오류");
        }
    }


    /**
     *
     * @param request 요청
     */
    public String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = "";
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access-token")) {
                accessToken = cookie.getValue();
                break;
            }
        }
        return accessToken;
    }

    public CartItem getBook(Long bookId) {
        try {
            ResponseEntity<CartItem> responseEntity = cartAdapter.getBook(bookId);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return responseEntity.getBody();
            } else {
                // 필요한 에러 처리 로직 추가
                throw new RuntimeException("API 호출 실패: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            // 예외 로깅 및 처리
            throw new RuntimeException("API 호출 중 예외 발생", e);
        }
    }

    public void saveCartInDBUseRequest(HttpServletRequest request) {
        String accessToken = getAccessToken(request);

        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new RuntimeException("세션이 없습니다.");
        }

        // 세션 ID 가져오기
        String sessionId = session.getId();

        // Redis에서 cartItem 리스트 가져오기
        Map<String, Object> cartData = (Map<String, Object>) redisTemplate.opsForValue().get(sessionId);
        Object cartItemsObj = cartData.get("cartItems");

        List<CartItem> cartItems = (List<CartItem>) cartItemsObj;

        // cartRequests 생성
        List<CartRequest> cartRequests = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Long id = cartItem.getId();
            int quantity = cartItem.getQuantity();
            cartRequests.add(new CartRequest(id, quantity));
        }

        try {
            // 데이터베이스에 저장 요청
            ResponseEntity<Void> response = cartAdapter.saveCartInDBUseHeader(BEARER + accessToken, cartRequests); // customerId 대신 sessionId 사용

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new AddressFailedException("장바구니 저장에 실패하였습니다.");
            }
        } catch (FeignException e) {
            throw new RuntimeException("요청 오류: " + e.getMessage(), e);
        }
    }
}