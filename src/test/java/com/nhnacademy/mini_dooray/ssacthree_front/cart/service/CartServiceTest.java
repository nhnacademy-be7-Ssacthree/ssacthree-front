package com.nhnacademy.mini_dooray.ssacthree_front.cart.service;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.adapter.CartAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private HttpServletRequest request;

    @Mock
    private CartAdapter cartAdapter;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartService cartService;

    private static final String CART_ID = "guestCart:1234567890";
    private List<CartItem> cartItems;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, "Book Title", 1, 20000, "image_url"));

        // Redis 기본 모킹
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // 세션 기본 설정
        when(request.getSession(false)).thenReturn(session);
        when(session.getId()).thenReturn(CART_ID);
    }

    @Test
    void testGetCartItemsByCartId_ExistingCart() {
        Map<String, Object> cartData = new HashMap<>();
        cartData.put("cartItems", cartItems);
        when(valueOperations.get(CART_ID)).thenReturn(cartData);

        List<CartItem> result = cartService.getCartItemsByCartId(CART_ID);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Book Title", result.get(0).getTitle());
    }

    @Test
    void testInitializeCart_NewSession() {
        when(request.getSession(false)).thenReturn(null); // 세션이 없는 경우
        when(request.getSession(true)).thenReturn(session);

        List<CartItem> result = cartService.initializeCart(request);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(session, times(1)).getId();
        verify(valueOperations, times(1)).set(eq(CART_ID), anyMap(), anyLong(), eq(TimeUnit.MINUTES));
    }

    @Test
    void testSaveCart_ValidData() {
        cartService.saveCart(CART_ID, cartItems, 1L);

        verify(valueOperations, times(1)).set(eq(CART_ID), anyMap(), eq(30L), eq(TimeUnit.MINUTES));
    }

    @Test
    void testUpdateItemQuantity_ValidUpdate() {
        // Mock HttpServletRequest 및 HttpSession 설정
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(CART_ID);

        // Mock 데이터 설정
        Map<String, Object> cartData = new HashMap<>();
        cartData.put("cartItems", cartItems);
        when(valueOperations.get(CART_ID)).thenReturn(cartData);

        // 메서드 호출
        cartService.updateItemQuantity(request, 1L, 2);

        // 결과 검증
        verify(valueOperations, times(1)).set(eq(CART_ID), anyMap(), eq(30L), eq(TimeUnit.MINUTES));
    }

    @Test
    void testAddNewBook_NewBook() {
        // Mock Redis 데이터 구조
        Map<String, Object> cartData = new HashMap<>();
        cartData.put("cartItems", cartItems);
        when(valueOperations.get(CART_ID)).thenReturn(cartData);

        // Mock HttpServletRequest 및 HttpSession 설정
        when(request.getSession(false)).thenReturn(session); // 세션이 이미 있는 경우
        when(request.getSession(true)).thenReturn(session);  // 새 세션 생성 시 반환
        when(session.getId()).thenReturn(CART_ID);           // 세션 ID 반환

        // Mock HttpSession 추가 설정 (세션이 null이 아님을 보장)
        when(request.getSession()).thenReturn(session);

        // 테스트 수행
        cartService.addNewBook(request, 2L, "New Book", 1, 15000, "new_image_url");

        // Redis에 데이터가 저장되었는지 검증
        verify(valueOperations, times(1)).set(eq(CART_ID), anyMap(), eq(30L), eq(TimeUnit.MINUTES));
    }

    @Test
    void testGetCustomerIdByCartId_ExistingCustomerId() {
        Map<String, Object> cartData = new HashMap<>();
        cartData.put("customerId", 1L);
        when(valueOperations.get(CART_ID)).thenReturn(cartData);

        Long customerId = cartService.getCustomerIdByCartId(CART_ID);

        assertNotNull(customerId);
        assertEquals(1L, customerId);
    }

    @Test
    void testGetCustomerIdByCartId_NoCustomerId() {
        Map<String, Object> cartData = new HashMap<>();
        when(valueOperations.get(CART_ID)).thenReturn(cartData);

        Long customerId = cartService.getCustomerIdByCartId(CART_ID);

        assertNull(customerId);
    }

    @Test
    void testDeleteItem_ItemExists() {
        // Mock HttpSession 설정
        when(request.getSession()).thenReturn(session); // 세션 반환
        when(session.getId()).thenReturn(CART_ID); // 세션 ID 설정

        // Mock 데이터 설정
        Map<String, Object> cartData = new HashMap<>();
        cartData.put("cartItems", cartItems); // 기존 cartItems 설정
        when(valueOperations.get(CART_ID)).thenReturn(cartData);

        // 메서드 호출
        cartService.deleteItem(request, 1L);

        // 결과 검증
        verify(valueOperations, times(1)).set(eq(CART_ID), anyMap(), eq(30L), eq(TimeUnit.MINUTES));
    }

    @Test
    void testDeleteItem_ItemNotExists() {
        // Mock HttpSession 설정
        when(request.getSession()).thenReturn(session); // 세션 반환
        when(session.getId()).thenReturn(CART_ID); // 세션 ID 설정

        // Mock 데이터 설정
        List<CartItem> emptyCartItems = new ArrayList<>();
        Map<String, Object> cartData = new HashMap<>();
        cartData.put("cartItems", emptyCartItems);
        when(valueOperations.get(CART_ID)).thenReturn(cartData);

        // 메서드 호출
        cartService.deleteItem(request, 99L); // 존재하지 않는 아이템 ID

        // 결과 검증
        verify(valueOperations, times(1)).set(eq(CART_ID), anyMap(), eq(30L), eq(TimeUnit.MINUTES));
    }

    @Test
    void testSaveCartInDB_Success() {
        List<CartItem> cartItemsToSave = new ArrayList<>();
        cartItemsToSave.add(new CartItem(1L, "Item 1", 2, 2000, "image1"));
        when(cartAdapter.saveCartInDB(anyList(), anyLong())).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<Void> response = cartService.saveCartInDB(cartItemsToSave, 1L);

        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        verify(cartAdapter, times(1)).saveCartInDB(anyList(), anyLong());
    }

    @Test
    void testSaveCartInDB_Failure() {
        List<CartItem> cartItemsToSave = new ArrayList<>();
        cartItemsToSave.add(new CartItem(1L, "Item 1", 2, 2000, "image1"));
        when(cartAdapter.saveCartInDB(anyList(), anyLong()))
            .thenThrow(new RuntimeException("API 요청 오류"));

        assertThrows(RuntimeException.class, () -> cartService.saveCartInDB(cartItemsToSave, 1L));
        verify(cartAdapter, times(1)).saveCartInDB(anyList(), anyLong());
    }

    @Test
    void testGetMemberCart_Success() {
        // Mock 쿠키 설정
        Cookie accessTokenCookie = new Cookie("access-token", "sampleAccessToken");
        when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie}); // 쿠키 반환 설정

        // Mock HttpSession 설정
        when(request.getSession(false)).thenReturn(session); // 세션이 존재하는 경우 반환
        when(request.getSession(true)).thenReturn(session);  // 세션 생성 요청 시 반환
        when(session.getId()).thenReturn(CART_ID);

        // Mock Redis 데이터 설정
        List<CartItem> responseCartItems = new ArrayList<>();
        responseCartItems.add(new CartItem(1L, "Member Item", 1, 3000, "image_member"));
        when(cartAdapter.getCartItems(anyString())).thenReturn(ResponseEntity.ok(responseCartItems));

        // Mock cartAdapter.getCustomerId() 설정
        when(cartAdapter.getCustomerId(anyString())).thenReturn(ResponseEntity.ok(123L));

        // 메서드 호출
        cartService.getMemberCart(request);

        // 결과 검증
        verify(valueOperations, times(1)).set(eq(CART_ID), anyMap(), eq(30L), eq(TimeUnit.MINUTES));
    }

    @Test
    void testGetAccessToken_ValidCookie() {
        Cookie[] cookies = {
            new Cookie("access-token", "test-token"),
            new Cookie("other-cookie", "value")
        };
        when(request.getCookies()).thenReturn(cookies);

        String accessToken = cartService.getAccessToken(request);

        assertEquals("test-token", accessToken);
    }

    @Test
    void testGetAccessToken_NoAccessTokenCookie() {
        Cookie[] cookies = {new Cookie("other-cookie", "value")};
        when(request.getCookies()).thenReturn(cookies);

        String accessToken = cartService.getAccessToken(request);

        assertEquals("", accessToken);
    }

    @Test
    void testGetBook_Success() {
        // Mock 데이터 설정
        CartItem mockCartItem = new CartItem(1L, "Test Book", 1, 10000, "image_url");
        ResponseEntity<CartItem> mockResponse = ResponseEntity.ok(mockCartItem);

        // cartAdapter Mock 설정
        when(cartAdapter.getBook(1L)).thenReturn(mockResponse);

        // 메서드 호출
        CartItem result = cartService.getBook(1L);

        // 결과 검증
        assertNotNull(result);
        assertEquals("Test Book", result.getTitle());
        assertEquals(10000, result.getPrice());
        verify(cartAdapter, times(1)).getBook(1L);
    }

    @Test
    void testGetBook_Failure() {
        // cartAdapter Mock 설정
        when(cartAdapter.getBook(1L))
            .thenThrow(new RuntimeException("API 호출 실패"));

        // 메서드 호출 및 예외 검증
        Exception exception = assertThrows(RuntimeException.class, () -> cartService.getBook(1L));
        assertTrue(exception.getMessage().contains("API 호출 중 예외 발생"));
        verify(cartAdapter, times(1)).getBook(1L);
    }

    @Test
    void testSaveCartInDBUseRequest_Success() {
        // Mock 데이터 설정
        CartItem mockCartItem = new CartItem(1L, "Test Book", 2, 10000, "image_url");
        List<CartItem> cartItems = Collections.singletonList(mockCartItem);
        Map<String, Object> mockCartData = new HashMap<>();
        mockCartData.put("cartItems", cartItems);

        ResponseEntity<Void> mockResponse = ResponseEntity.ok().build();

        Cookie[] mockCookies = new Cookie[]{ new Cookie("access-token", "mockAccessToken") };

        // Redis 및 cartAdapter Mock 설정
        when(request.getSession(false)).thenReturn(session);
        when(session.getId()).thenReturn(CART_ID);
        when(redisTemplate.opsForValue().get(CART_ID)).thenReturn(mockCartData);
        when(cartAdapter.saveCartInDBUseHeader(anyString(), anyList())).thenReturn(mockResponse);
        when(request.getCookies()).thenReturn(mockCookies); // 쿠키 설정

        // 메서드 호출
        cartService.saveCartInDBUseRequest(request);

        // 결과 검증
        verify(cartAdapter, times(1)).saveCartInDBUseHeader(eq("Bearer mockAccessToken"), anyList());
        verify(redisTemplate.opsForValue(), times(1)).get(CART_ID);
    }

}