package com.nhnacademy.mini_dooray.ssacthree_front.cart.service;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.adapter.CartAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.exception.CartFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.exception.SessionNotFoundException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.MemberNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

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
        assertEquals("Book Title", result.getFirst().getTitle());
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
        List<CartItem> cartItems1 = Collections.singletonList(mockCartItem);
        Map<String, Object> mockCartData = new HashMap<>();
        mockCartData.put("cartItems", cartItems1);

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

    @Test
    void testGetRandomBook_Success() {
        Long bookId = 1L;
        String accessToken = "dummy-access-token";
        CartItem expectedCartItem = new CartItem(bookId, "Random Book", 1, 15000, "random_image_url");

        // Mock: request에서 access-token 쿠키 반환
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access-token", accessToken)});
        // Mock: cartAdapter.getRandomBook 호출 성공
        when(cartAdapter.getRandomBook("Bearer " + accessToken, bookId))
            .thenReturn(new ResponseEntity<>(expectedCartItem, HttpStatus.OK));

        CartItem actualCartItem = cartService.getRandomBook(bookId, request);

        assertNotNull(actualCartItem);
        assertEquals(expectedCartItem.getId(), actualCartItem.getId());
        assertEquals(expectedCartItem.getTitle(), actualCartItem.getTitle());
        assertEquals(expectedCartItem.getQuantity(), actualCartItem.getQuantity());
        assertEquals(expectedCartItem.getPrice(), actualCartItem.getPrice());
        assertEquals(expectedCartItem.getBookThumbnailImageUrl(), actualCartItem.getBookThumbnailImageUrl());

        verify(cartAdapter, times(1)).getRandomBook("Bearer " + accessToken, bookId);
    }

    @Test
    void testGetRandomBook_Fail() {
        // Given
        Long bookId = 1L;
        String accessToken = "dummy-access-token";

        // Mock: request에서 access-token 쿠키 반환
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("access-token", accessToken)});
        // Mock: cartAdapter.getRandomBook 호출 시 HttpClientErrorException 발생
        when(cartAdapter.getRandomBook("Bearer " + accessToken, bookId))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
            cartService.getRandomBook(bookId, request)
        );
        assertEquals("책을 가져오는데 실패하였습니다.", exception.getMessage());

        // Verify
        verify(cartAdapter, times(1)).getRandomBook("Bearer " + accessToken, bookId);
    }

    @Test
    void testGetCartItemsByCartId_EmptyCart() {
        when(valueOperations.get(CART_ID)).thenReturn(null);

        List<CartItem> result = cartService.getCartItemsByCartId(CART_ID);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSaveCartInDBUseRequest_Failure() {
        Cookie[] mockCookies = {new Cookie("access-token", "mockAccessToken")};
        Map<String, Object> cartData = new HashMap<>();
        cartData.put("cartItems", cartItems);

        when(request.getCookies()).thenReturn(mockCookies);
        when(request.getSession(false)).thenReturn(session);
        when(session.getId()).thenReturn(CART_ID);
        when(redisTemplate.opsForValue().get(CART_ID)).thenReturn(cartData);
        when(cartAdapter.saveCartInDBUseHeader(anyString(), anyList()))
            .thenThrow(new RuntimeException("API 실패"));

        try {
            cartService.saveCartInDBUseRequest(request);
        } catch (RuntimeException e) {
            System.out.println("Exception Message: " + e.getMessage()); // 디버깅 로그
        }

        // Assertion 실패 디버깅
        verify(cartAdapter, times(1)).saveCartInDBUseHeader(anyString(), anyList());
        verify(redisTemplate.opsForValue(), times(1)).get(CART_ID);
    }

    @Test
    void testGetMemberCart_Failure() {
        // Mock 쿠키 설정
        Cookie accessTokenCookie = new Cookie("access-token", "mockAccessToken");
        when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});

        // Mock 세션 설정
        when(request.getSession(false)).thenReturn(session); // 기존 세션 반환
        when(request.getSession(true)).thenReturn(session);  // 새 세션 생성 시 반환
        when(session.getId()).thenReturn(CART_ID);

        // Mock 예외 설정
        when(cartAdapter.getCartItems(anyString()))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Mocked BAD_REQUEST"));

        // 예외 발생 및 검증
        RuntimeException exception = assertThrows(CartFailedException.class, () ->
            cartService.getMemberCart(request)
        );

        // 예외 메시지 검증
        assertTrue(exception.getMessage().contains("회원 카트를 가져오는데 실패하였습니다."));

        // Mock 호출 검증
        verify(cartAdapter, times(1)).getCartItems(anyString());
    }

    @Test
    void testDeleteItem_NoSession() {
        // Mock 세션이 없는 상태
        when(request.getSession(false)).thenReturn(null);

        // deleteItem 호출 시 예외 발생 확인
        SessionNotFoundException exception = assertThrows(SessionNotFoundException.class, () ->
            cartService.deleteItem(request, 1L)
        );

        // 예외 메시지 검증
        assertTrue(exception.getMessage().contains("세션이 없습니다."));

        // Redis 호출이 발생하지 않았는지 검증
        verify(valueOperations, never()).set(anyString(), anyMap(), anyLong(), eq(TimeUnit.MINUTES));
    }

    @Test
    void testUpdateItemQuantity_NoItem() {
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn(CART_ID);
        when(valueOperations.get(CART_ID)).thenReturn(null);

        cartService.updateItemQuantity(request, 99L, 2);

        verify(valueOperations, times(1)).set(eq(CART_ID), anyMap(), eq(30L), eq(TimeUnit.MINUTES));
    }

    @Test
    void testAddNewBook_ExistingBook() {
        // Mock Redis 데이터 구조
        Map<String, Object> cartData = new HashMap<>();
        cartData.put("cartItems", cartItems);
        when(valueOperations.get(CART_ID)).thenReturn(cartData);

        // Mock HttpServletRequest 및 HttpSession 설정
        when(request.getSession(false)).thenReturn(session); // 세션이 null이 아님을 보장
        when(session.getId()).thenReturn(CART_ID);           // 세션 ID 반환

        // 테스트 수행
        cartService.addNewBook(request, 1L, "Book Title", 1, 20000, "image_url");

        // Redis에 데이터가 저장되었는지 검증
        verify(valueOperations, times(1)).set(eq(CART_ID), anyMap(), eq(30L), eq(TimeUnit.MINUTES));
    }

    @Test
    void testAddNewBook_NoSession() {
        // Mock 세션이 없는 상태
        when(request.getSession(false)).thenReturn(null);

        // 예외 발생 검증
        SessionNotFoundException exception = assertThrows(SessionNotFoundException.class, () ->
            cartService.addNewBook(request, 1L, "Book Title", 1, 20000, "image_url")
        );

        assertTrue(exception.getMessage().contains("세션이 없습니다."));
    }

    @Test
    void testGetLoginCustomerId_Success() {
        Cookie accessTokenCookie = new Cookie("access-token", "mockAccessToken");
        when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});
        when(cartAdapter.getCustomerId(anyString())).thenReturn(ResponseEntity.ok(123L));

        Long customerId = cartService.getLoginCustomerId(request);

        assertNotNull(customerId);
        assertEquals(123L, customerId);
    }

    @Test
    void testGetLoginCustomerId_Failure() {
        // Mock 쿠키 설정
        Cookie accessTokenCookie = new Cookie("access-token", "mockAccessToken");
        when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});

        // Mock Adapter 예외 설정
        when(cartAdapter.getCustomerId(anyString()))
            .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Mocked BAD_REQUEST"));

        // 예외 발생 및 검증
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () ->
            cartService.getLoginCustomerId(request)
        );

        // 예외 메시지 검증
        assertTrue(exception.getMessage().contains("아이디를 가져올 수 없습니다.")); // 메시지 검증
    }

    @Test
    void testInitializeCart_NoSession() {
        // 세션이 null을 반환
        when(request.getSession(false)).thenReturn(null);
        when(request.getSession(true)).thenReturn(session);
        when(session.getId()).thenReturn(CART_ID);

        // 호출
        List<CartItem> result = cartService.initializeCart(request);

        // 결과 검증
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(valueOperations, times(1)).set(eq(CART_ID), anyMap(), anyLong(), eq(TimeUnit.MINUTES));
    }

    @Test
    void testGetLoginCustomerId_InvalidAccessToken() {
        // Mock 잘못된 AccessToken
        Cookie accessTokenCookie = new Cookie("access-token", "invalidAccessToken");
        when(request.getCookies()).thenReturn(new Cookie[]{accessTokenCookie});

        // Mock Adapter 예외
        when(cartAdapter.getCustomerId(anyString()))
            .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        // 예외 발생 검증
        MemberNotFoundException exception = assertThrows(MemberNotFoundException.class, () ->
            cartService.getLoginCustomerId(request)
        );

        // 예외 메시지 검증
        assertTrue(exception.getMessage().contains("아이디를 가져올 수 없습니다."));
    }

    @Test
    void testSaveCart_RedisFailure() {
        // RedisTemplate의 set 호출 시 예외 발생 설정
        doThrow(new RuntimeException("Redis 연결 실패"))
            .when(valueOperations).set(anyString(), anyMap(), anyLong(), eq(TimeUnit.MINUTES));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cartService.saveCart(CART_ID, cartItems, 1L);
        });

        // 예외 메시지 확인
        assertTrue(exception.getMessage().contains("Redis 연결 실패"));
    }
}