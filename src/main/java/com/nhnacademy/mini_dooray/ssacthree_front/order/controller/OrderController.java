package com.nhnacademy.mini_dooray.ssacthree_front.order.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.adapter.PackagingCustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service.PackagingService;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.impl.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class OrderController {

    private OrderServiceImpl orderService;

    private final CartService cartService;
    private final PackagingService packagingService;


    // 1. 비회원, 회원 주문 페이지 이동 -> 각각 다르게 처리?
    // 장바구니 -> 주문
    // 책 -> 바로 주문

    @GetMapping("/orderSheet")
    public String orderSheet(HttpServletRequest request, Model model) {
        //회원 주문인지 비회원 주문인지 확인

        //배송비 정책 끌어오기 - 현재 사용중인 ture인거(api요청)


        // 일단 비회원 장바구니부터 처리? 회원일때랑 비회원일때 response다름
        // 이 세션에서 attribute의 cartId 알아낸 다음 redis에서 cartId로 상품 정보 가져오기(아이디랑 수량만 받으면 안되나?)
//        GuestCartInfoResponse guestCartInfoResponse = orderService.createGusetOrderSheet(session);

        List<CartItem> cartItems = cartService.initializeCart(request); // 서비스에서 장바구니 초기화
        // 비회원 장바구니 정보 넣어주기
//        model.addAttribute("guestCartInfo", guestCartInfoResponse);

        // 리스트 형식으로 레디스의 상품 정보들 들어감.
        model.addAttribute("guestCartInfo", cartItems);

        // 포장지 가져오기
        List<PackagingGetResponse> packagingList = packagingService.getAllCustomerPackaging();
        model.addAttribute("packagingList", packagingList);

        // 임의의 주문 번호호 생성해서 model에 넣기

        return "order/orderSheet";
    }

    // 2. 비회원, 회원 장바구니 주문 구현
    @PostMapping("/order")
    public String order(@ModelAttribute OrderCreateRequest orderCreateRequest, Model model) {
        // 트랜잭션 시작

        // 재고 체크? - or 장바구니에서 ?

        // 주문 저장 API 호출 - 주문 DB에 저장 + 주문 상세 저장

        // 결제시 사용한 쿠폰, 포인트 차감

        // 쿠폰 포인트 내역 생성

        // 재고 차감

        // 장바구니 비우기

        // 트랜잭션 커밋, 실패시 롤백 -> 까지가 API로 뒷단에서 처리

        // 후에 결제, 모델에 넣고 토스 페이먼트 결제창으로 이제 이동
        return "payment/checkout";
    }

    // 3. 비회원, 회원 바로 주문 구현


    // 4. 비회원, 회원 주문 내역 페이지 구현

}
