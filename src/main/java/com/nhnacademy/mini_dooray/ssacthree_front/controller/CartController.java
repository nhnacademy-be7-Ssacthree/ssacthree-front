package com.nhnacademy.mini_dooray.ssacthree_front.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private static final String CART_REDIRECT = "redirect:/shop/carts";


    @GetMapping("/shop/carts")
    public String viewCart(HttpServletRequest request, Model model) {
        List<CartItem> cartItems = cartService.initializeCart(request); // 서비스에서 장바구니 초기화
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", cartService.calculateTotalPrice(cartItems));
        return "cart";
    }

    @PostMapping("/shop/carts")
    public String addNewBook(HttpServletRequest request, @RequestParam Long itemId,
                                                  @RequestParam String title,
                                                  @RequestParam int quantity,
                                                  @RequestParam int price,
                                                  @RequestParam String image) { //새로운 책 장바구니에 추가
        cartService.addNewBook(request,itemId,title,quantity,price,image);
        return CART_REDIRECT;
    }

    @PutMapping("/shop/carts/{itemId}")
    public String changeQuantity(HttpServletRequest request, @PathVariable Long itemId, @RequestParam int quantityChange) { //수량 변경
        cartService.updateItemQuantity(request, itemId, quantityChange);
        return CART_REDIRECT; // 변경 후 장바구니 페이지로 리다이렉트
    }


    @DeleteMapping("/shop/carts/{itemId}")
    public String deleteCartItem(HttpServletRequest request, @PathVariable Long itemId) {
        cartService.deleteItem(request,itemId);
        return CART_REDIRECT;
    }

    @GetMapping("/shop/carts/{bookId}") // 장바구니에 필요한 책 데이터 가져오기 나중에 삭제 예정
    public String getBookInDB(HttpServletRequest request, @PathVariable Long bookId) {
        CartItem cartItem = cartService.getRandomBook(bookId, request);
        cartService.addNewBook(request,cartItem.getId(),cartItem.getTitle(),1,cartItem.getPrice(),cartItem.getImageUrl());
        return CART_REDIRECT;
    }

    @GetMapping("/shop/carts/customers")
    public String makeLoginSession(HttpServletRequest request){
        cartService.getMemberCart(request);
        return "redirect:/";
    }


    @PostMapping("/shop/carts/cart")
    public void saveInDB(List<CartItem> cartItems, Long customerId) {
        cartService.saveCartInDB(cartItems,customerId);
    }

    @GetMapping("/shop/carts/add")
    public String getBookAndSaveInRedis(HttpServletRequest request, @RequestParam String bookId){
        Long longBookId = Long.parseLong(bookId);
        CartItem cartItem = cartService.getBook(longBookId);
        cartService.addNewBook(request,cartItem.getId(),cartItem.getTitle(),1,cartItem.getPrice(),cartItem.getImageUrl());
        return "redirect:/shop/carts";
    }
}