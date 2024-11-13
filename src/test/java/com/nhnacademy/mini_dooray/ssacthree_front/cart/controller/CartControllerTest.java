//package com.nhnacademy.mini_dooray.ssacthree_front.cart.controller;
//
//import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
//import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
//import com.nhnacademy.mini_dooray.ssacthree_front.controller.CartController;
//import jakarta.servlet.http.HttpSession;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.ui.Model;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(CartController.class)
//class CartControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private CartService cartService;
//
//    @Mock
//    private HttpSession session;
//
//    @Mock
//    private Model model;
//
//    private List<CartItem> cartItems;
//
//    @BeforeEach
//    void setup() {
//        MockitoAnnotations.openMocks(this);
//        cartItems = new ArrayList<>();
//        cartItems.add(new CartItem(1L, "Book Title", 2, 10000, null));
//    }
//
//    @Test
//    void testViewCart() throws Exception {
//        when(cartService.initializeCart(any(HttpSession.class))).thenReturn(cartItems);
//        when(cartService.calculateTotalPrice(cartItems)).thenReturn(20000);
//
//        mockMvc.perform(get("/shop"))
//            .andExpect(status().isOk())
//            .andExpect(view().name("cart"))
//            .andExpect(model().attribute("cartItems", cartItems))
//            .andExpect(model().attribute("totalPrice", 20000));
//
//        verify(cartService, times(1)).initializeCart(any(HttpSession.class));
//        verify(cartService, times(1)).calculateTotalPrice(cartItems);
//    }
//
//    @Test
//    void testAddNewBook() throws Exception {
//        mockMvc.perform(post("/shop/")
//                .param("itemId", "1")
//                .param("title", "New Book")
//                .param("price", "15000")
//                .param("image", new byte[0].toString())
//                .sessionAttr("session", session)
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(redirectedUrl("/shop?accessTokenExists=false"));
//
//        verify(cartService, times(1)).addNewBook(any(HttpSession.class), eq(1L), eq("New Book"), eq(15000), any(byte[].class));
//    }
//
//    @Test
//    void testChangeQuantity() throws Exception {
//        mockMvc.perform(put("/shop/1")
//                .param("quantityChange", "1")
//                .sessionAttr("session", session))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(redirectedUrl("/shop?accessTokenExists=false"));
//
//        verify(cartService, times(1)).updateItemQuantity(any(HttpSession.class), eq(1L), eq(1));
//    }
//
//    @Test
//    void testDeleteCartItem() throws Exception {
//        mockMvc.perform(delete("/shop/1")
//                .sessionAttr("session", session))
//            .andExpect(status().is3xxRedirection())
//            .andExpect(redirectedUrl("/shop?accessTokenExists=false"));
//
//        verify(cartService, times(1)).deleteItem(any(HttpSession.class), eq(1L));
//    }
//}
//
