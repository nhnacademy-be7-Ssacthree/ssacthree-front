package com.nhnacademy.mini_dooray.ssacthree_front.cart;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @Mock
    private HttpSession session;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testGetCartItemsByCartId() {
        List<CartItem> mockCartItems = new ArrayList<>();
        mockCartItems.add(new CartItem(1L, "책 제목 1", 1, 20000, null));
        when(valueOperations.get(anyString())).thenReturn(mockCartItems);

        List<CartItem> cartItems = cartService.getCartItemsByCartId("testCartId");
        assertEquals(1, cartItems.size());
        assertEquals("책 제목 1", cartItems.get(0).getTitle());
    }

    @Test
    void testInitializeCart_NewCart() {
        when(session.getAttribute("cartId")).thenReturn(null).thenReturn("generatedCartId");
        doNothing().when(session).setAttribute(eq("cartId"), anyString());

        List<CartItem> cartItems = cartService.initializeCart(session);
        assertNotNull(cartItems);
        assertFalse(cartItems.isEmpty());
    }

    @Test
    void testCalculateTotalPrice() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, "책 제목 1", 2, 15000, null));
        cartItems.add(new CartItem(2L, "책 제목 2", 1, 25000, null));

        int totalPrice = cartService.calculateTotalPrice(cartItems);
        assertEquals(55000, totalPrice);
    }

    @Test
    void testUpdateItemQuantity() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, "책 제목 1", 1, 20000, null));

        when(session.getAttribute("cartId")).thenReturn("testCartId");
        when(valueOperations.get("testCartId")).thenReturn(cartItems);

        cartService.updateItemQuantity(session, 1L, 2);

        ArgumentCaptor<List<CartItem>> captor = ArgumentCaptor.forClass(List.class);
        verify(valueOperations, times(1)).set(eq("testCartId"), captor.capture(), eq(3L), eq(TimeUnit.HOURS));

        List<CartItem> updatedCartItems = captor.getValue();

        assertEquals(3, updatedCartItems.get(0).getQuantity());
    }

    @Test
    void testDeleteItem() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, "책 제목 1", 1, 20000, null));
        when(session.getAttribute("cartId")).thenReturn("testCartId");
        when(valueOperations.get("testCartId")).thenReturn(cartItems);

        cartService.deleteItem(session, 1L);

        verify(valueOperations).set(eq("testCartId"), anyList(), eq(3L), eq(TimeUnit.HOURS));
        assertTrue(cartItems.isEmpty());
    }

    @Test
    void testAddNewBook() {
        List<CartItem> cartItems = new ArrayList<>();
        when(session.getAttribute("cartId")).thenReturn("testCartId");
        when(valueOperations.get("testCartId")).thenReturn(cartItems);

        cartService.addNewBook(session, 2L, "책 제목 2", 25000, null);

        verify(valueOperations).set(eq("testCartId"), anyList(), eq(3L), eq(TimeUnit.HOURS));
        assertEquals(1, cartItems.size());
        assertEquals("책 제목 2", cartItems.get(0).getTitle());
    }
}