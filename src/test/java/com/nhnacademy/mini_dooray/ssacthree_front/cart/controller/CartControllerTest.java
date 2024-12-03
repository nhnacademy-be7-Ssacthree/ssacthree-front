package com.nhnacademy.mini_dooray.ssacthree_front.cart.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    private List<CartItem> cartItems;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        cartItems = new ArrayList<>();
        cartItems.add(new CartItem(1L, "Book Title", 2, 10000, null));
    }

    @Test
    @WithMockUser // Spring Security 인증을 무시하고 테스트를 실행
    void testViewCart() throws Exception {
        when(cartService.initializeCart(any(HttpServletRequest.class))).thenReturn(cartItems);

        mockMvc.perform(get("/shop/carts"))
            .andExpect(status().isOk())
            .andExpect(view().name("cart"))
            .andExpect(model().attribute("cartItems", cartItems));

        verify(cartService, times(1)).initializeCart(any(HttpServletRequest.class));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"}) // 인증된 사용자 추가
    void testAddNewBook() throws Exception {
        mockMvc.perform(post("/shop/carts")
                .param("itemId", "1")
                .param("title", "New Book")
                .param("quantity", "1")
                .param("price", "15000")
                .param("image", "image_url")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .with(csrf())) // CSRF 토큰 추가
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/shop/carts"));

        verify(cartService, times(1)).addNewBook(any(HttpServletRequest.class), eq(1L), eq("New Book"), eq(1), eq(15000), eq("image_url"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"}) // 인증된 사용자 추가
    void testChangeQuantity() throws Exception {
        mockMvc.perform(put("/shop/carts/1")
                .param("quantityChange", "1")
                .with(csrf())) // CSRF 토큰 추가
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/shop/carts"));

        verify(cartService, times(1)).updateItemQuantity(any(HttpServletRequest.class), eq(1L), eq(1));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testDeleteCartItem() throws Exception {
        mockMvc.perform(delete("/shop/carts/1")
                .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/shop/carts"));

        verify(cartService, times(1)).deleteItem(any(HttpServletRequest.class), eq(1L));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testGetBookInDB() throws Exception {
        // Given
        Long bookId = 1L;
        CartItem cartItem = new CartItem(bookId, "Book Title", 1, 20000, "image_url");

        when(cartService.getRandomBook(eq(bookId), any(HttpServletRequest.class))).thenReturn(cartItem);

        // When & Then
        mockMvc.perform(get("/shop/carts/{bookId}", bookId))
            .andExpect(status().is3xxRedirection()) // Redirect 상태 확인
            .andExpect(redirectedUrl("/shop/carts")); // Redirect URL 확인

        verify(cartService, times(1)).getRandomBook(eq(bookId), any(HttpServletRequest.class));
        verify(cartService, times(1)).addNewBook(any(HttpServletRequest.class), eq(cartItem.getId()), eq(cartItem.getTitle()),
            eq(1), eq(cartItem.getPrice()), eq(cartItem.getBookThumbnailImageUrl()));
    }

    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testMakeLoginSession() throws Exception {
        // When & Then
        mockMvc.perform(get("/shop/members/carts"))
            .andExpect(status().is3xxRedirection()) // Redirect 상태 확인
            .andExpect(redirectedUrl("/")); // Redirect URL 확인

        verify(cartService, times(1)).getMemberCart(any(HttpServletRequest.class));
    }


    @Test
    @WithMockUser(username = "testUser", roles = {"MEMBER"})
    void testGetBookAndSaveInRedis() throws Exception {
        // Given
        String bookId = "1";
        String quantity = "2";
        CartItem cartItem = new CartItem(1L, "Book Title", 2, 20000, "image_url");

        when(cartService.getBook(Long.parseLong(bookId))).thenReturn(cartItem);

        // When & Then
        mockMvc.perform(get("/shop/carts/add")
                .param("bookId", bookId)
                .param("quantity", quantity))
            .andExpect(status().is3xxRedirection()) // Redirect 상태 확인
            .andExpect(redirectedUrl("/shop/carts")); // Redirect URL 확인

        verify(cartService, times(1)).getBook(Long.parseLong(bookId));
        verify(cartService, times(1)).addNewBook(any(HttpServletRequest.class), eq(cartItem.getId()), eq(cartItem.getTitle()),
            eq(Integer.parseInt(quantity)), eq(cartItem.getPrice()), eq(cartItem.getBookThumbnailImageUrl()));
    }
}