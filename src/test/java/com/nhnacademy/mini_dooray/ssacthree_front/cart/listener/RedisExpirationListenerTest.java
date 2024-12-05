package com.nhnacademy.mini_dooray.ssacthree_front.cart.listener;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.controller.CartController;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.data.redis.core.ValueOperations;

import static org.mockito.Mockito.*;

class RedisExpirationListenerTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private CartController cartController;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private RedisExpirationListener redisExpirationListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // RedisTemplate의 opsForValue Mock 설정
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testOnMessage_WithValidCartData() {
        // Given
        String expiredKey = "alert:cart123";
        Message mockMessage = mock(Message.class);
        when(mockMessage.toString()).thenReturn(expiredKey);

        Map<String, Object> mockCartData = new HashMap<>();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, "Book Title", 2, 1000, "image_url"));
        mockCartData.put("cartItems", cartItems);
        mockCartData.put("customerId", 123L);

        // RedisTemplate와 ValueOperations Mock 설정
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("cart123")).thenReturn(mockCartData);

        // When
        redisExpirationListener.onMessage(mockMessage, null);

        // Then
        verify(cartController, times(1)).saveInDB(cartItems, 123L);
    }

    @Test
    void testOnMessage_WithMissingCartItems() {
        // Given
        String expiredKey = "alert:cart123";
        Message mockMessage = mock(Message.class);
        when(mockMessage.toString()).thenReturn(expiredKey);

        Map<String, Object> mockCartData = new HashMap<>();
        mockCartData.put("customerId", 123L);

        when(redisTemplate.opsForValue().get("cart123")).thenReturn(mockCartData);

        // When
        redisExpirationListener.onMessage(mockMessage, null);

        // Then
        verify(cartController, never()).saveInDB(any(), any());
    }

    @Test
    void testOnMessage_WithMissingCustomerId() {
        // Given
        String expiredKey = "alert:cart123";
        Message mockMessage = mock(Message.class);
        when(mockMessage.toString()).thenReturn(expiredKey);

        Map<String, Object> mockCartData = new HashMap<>();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, "Book Title", 2, 1000, "image_url"));
        mockCartData.put("cartItems", cartItems);

        when(redisTemplate.opsForValue().get("cart123")).thenReturn(mockCartData);

        // When
        redisExpirationListener.onMessage(mockMessage, null);

        // Then
        verify(cartController, never()).saveInDB(any(), any());
    }

    @Test
    void testOnMessage_WithInvalidKey() {
        // Given
        String expiredKey = "invalidKey";
        Message mockMessage = mock(Message.class);
        when(mockMessage.toString()).thenReturn(expiredKey);

        // When
        redisExpirationListener.onMessage(mockMessage, null);

        // Then
        verify(redisTemplate, never()).opsForValue();
        verify(cartController, never()).saveInDB(any(), any());
    }

    @Test
    void testOnMessage_WithNullCartData() {
        // Given
        String expiredKey = "alert:cart123";
        Message mockMessage = mock(Message.class);
        when(mockMessage.toString()).thenReturn(expiredKey);

        when(redisTemplate.opsForValue().get("cart123")).thenReturn(null);

        // When
        redisExpirationListener.onMessage(mockMessage, null);

        // Then
        verify(cartController, never()).saveInDB(any(), any());
    }
}