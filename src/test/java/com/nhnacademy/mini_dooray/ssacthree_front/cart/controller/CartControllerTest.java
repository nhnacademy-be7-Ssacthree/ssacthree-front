package com.nhnacademy.mini_dooray.ssacthree_front.cart.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import com.nhnacademy.mini_dooray.ssacthree_front.controller.CartController;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    void testViewCart() throws Exception {
        when(cartService.initializeCart(any(HttpServletRequest.class))).thenReturn(cartItems);

        mockMvc.perform(get("/shop/carts"))
            .andExpect(status().isOk())
            .andExpect(view().name("cart"))
            .andExpect(model().attribute("cartItems", cartItems));

        verify(cartService, times(1)).initializeCart(any(HttpServletRequest.class));
    }

    @Test
    void testAddNewBook() throws Exception {
        mockMvc.perform(post("/shop/carts")
                .param("itemId", "1")
                .param("title", "New Book")
                .param("quantity", "1")
                .param("price", "15000")
                .param("image", "image_url")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/shop/carts"));

        verify(cartService, times(1)).addNewBook(any(HttpServletRequest.class), eq(1L), eq("New Book"), eq(1), eq(15000), eq("image_url"));
    }

    @Test
    void testChangeQuantity() throws Exception {
        mockMvc.perform(put("/shop/carts/1")
                .param("quantityChange", "1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/shop/carts"));

        verify(cartService, times(1)).updateItemQuantity(any(HttpServletRequest.class), eq(1L), eq(1));
    }

    @Test
    void testDeleteCartItem() throws Exception {
        mockMvc.perform(delete("/shop/carts/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/shop/carts"));

        verify(cartService, times(1)).deleteItem(any(HttpServletRequest.class), eq(1L));
    }
}
