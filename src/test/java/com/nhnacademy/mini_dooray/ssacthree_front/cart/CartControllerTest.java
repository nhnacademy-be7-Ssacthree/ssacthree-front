package com.nhnacademy.mini_dooray.ssacthree_front.cart;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import com.nhnacademy.mini_dooray.ssacthree_front.controller.CartController;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testViewCart() throws Exception {
        List<CartItem> mockCartItems = new ArrayList<>();
        mockCartItems.add(new CartItem(1L, "책 제목 1", 1, 20000, null));

        when(cartService.initializeCart(any(HttpSession.class))).thenReturn(mockCartItems);
        when(cartService.calculateTotalPrice(mockCartItems)).thenReturn(20000);

        mockMvc.perform(get("/shop"))
            .andExpect(status().isOk())
            .andExpect(view().name("cart"))
            .andExpect(model().attribute("cartItems", mockCartItems))
            .andExpect(model().attribute("totalPrice", 20000));
    }

    @Test
    void testAddNewBook() throws Exception {
        doNothing().when(cartService).addNewBook(any(HttpSession.class), anyLong(), anyString(), anyInt(), any());

        mockMvc.perform(post("/shop/")
                .param("itemId", "1")
                .param("title", "책 제목")
                .param("price", "25000")
                .param("image", ""))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/shop"));

        verify(cartService, times(1)).addNewBook(any(HttpSession.class), eq(1L), eq("책 제목"), eq(25000), any());
    }

    @Test
    void testChangeQuantity() throws Exception {
        doNothing().when(cartService).updateItemQuantity(any(HttpSession.class), anyLong(), anyInt());

        mockMvc.perform(put("/shop/1")
                .param("quantityChange", "2"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/shop"));

        verify(cartService, times(1)).updateItemQuantity(any(HttpSession.class), eq(1L), eq(2));
    }

    @Test
    void testDeleteCartItem() throws Exception {
        doNothing().when(cartService).deleteItem(any(HttpSession.class), anyLong());

        mockMvc.perform(delete("/shop/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/shop"));

        verify(cartService, times(1)).deleteItem(any(HttpSession.class), eq(1L));
    }
}
