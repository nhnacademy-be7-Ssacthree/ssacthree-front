//package com.nhnacademy.mini_dooray.ssacthree_front.cart.service;
//
//import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
//import jakarta.servlet.http.HttpSession;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class CartServiceTest {
//
//    @Mock
//    private RedisTemplate<String, Object> redisTemplate;
//
//    @Mock
//    private HttpSession session;
//
//    @Mock
//    private ValueOperations<String, Object> valueOperations;
//
//    @InjectMocks
//    private CartService cartService;
//
//    private static final String CART_ID = "guestCart:1234567890";
//    private List<CartItem> cartItems;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        cartItems = new ArrayList<>();
//        cartItems.add(new CartItem(1L, "Book Title", 1, 20000, null));
//
//        // 기본적으로 ValueOperations 모킹
//        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
//        when(session.getAttribute("cartId")).thenReturn(CART_ID);
//    }
//
//    @Test
//    void testGetCartItemsByCartId() {
//        when(valueOperations.get(CART_ID)).thenReturn(cartItems);
//        List<CartItem> result = cartService.getCartItemsByCartId(CART_ID);
//        assertEquals(1, result.size());
//        assertEquals("Book Title", result.getFirst().getTitle());
//    }
//
//    @Test
//    void testInitializeCart_NewCart() {
//        when(session.getAttribute("cartId")).thenReturn(null); // cartId가 없는 경우
//        when(valueOperations.get(anyString())).thenReturn(null);
//
//        List<CartItem> result = cartService.initializeCart(session);
//
//        assertNotNull(result);
//        assertFalse(result.isEmpty());
//        verify(session, times(1)).setAttribute(eq("cartId"), anyString());
//    }
//
//    @Test
//    void testSaveNewCart() {
//        cartService.saveNewCart(CART_ID, cartItems);
//        verify(valueOperations, times(1)).set(CART_ID, cartItems, 3, TimeUnit.HOURS);
//    }
//
//    @Test
//    void testCalculateTotalPrice() {
//        int totalPrice = cartService.calculateTotalPrice(cartItems);
//        assertEquals(20000, totalPrice);
//    }
//
//    @Test
//    void testUpdateItemQuantity_IncreaseQuantity() {
//        when(valueOperations.get(CART_ID)).thenReturn(cartItems);
//
//        cartService.updateItemQuantity(session, 1L, 1);
//
//        verify(valueOperations, times(1)).set(eq(CART_ID), anyList(), eq(3L), eq(TimeUnit.HOURS));
//    }
//
//    @Test
//    void testDeleteItem() {
//        when(valueOperations.get(CART_ID)).thenReturn(cartItems);
//
//        cartService.deleteItem(session, 1L);
//
//        verify(valueOperations, times(1)).set(eq(CART_ID), anyList(), eq(3L), eq(TimeUnit.HOURS));
//        assertTrue(cartItems.isEmpty());
//    }
//
//    @Test
//    void testAddNewBook_NewBook() {
//        when(valueOperations.get(CART_ID)).thenReturn(cartItems);
//
//        cartService.addNewBook(session, 2L, "New Book", 15000, null);
//
//        verify(valueOperations, times(1)).set(eq(CART_ID), anyList(), eq(3L), eq(TimeUnit.HOURS));
//    }
//}
//
