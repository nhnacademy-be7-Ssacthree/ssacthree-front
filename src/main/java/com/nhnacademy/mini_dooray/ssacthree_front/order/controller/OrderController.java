package com.nhnacademy.mini_dooray.ssacthree_front.order.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service.DeliveryRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.adapter.PackagingCustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service.PackagingService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.AddressService;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.BookOrderRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.impl.OrderServiceImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class OrderController {

    private OrderServiceImpl orderService;

    private final CartService cartService;
    private final PackagingService packagingService;
    private final MemberService memberService;
    private final AddressService addressService;
    private final BookCommonService bookCommonService;
    private final DeliveryRuleService deliveryRuleService;


    // 1. 비회원, 회원 주문 페이지 이동 -> 각각 다르게 처리?
    // 장바구니 -> 주문
    // 책 -> 바로 주문


    // 장바구니 주문
    @GetMapping("/order-cart")
    public String orderCart(HttpServletRequest request, Model model) {
        //회원 주문인지 비회원 주문인지 확인
        MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(request);
        // 상품 정보
        List<CartItem> cartItems = new ArrayList<>();

        // 여기도 수정 필요
        cartItems = cartService.initializeCart(request); // 서비스에서 장바구니 초기화

        // 비회원 - redis에서 끌어오기
        if (memberInfoResponse == null) {
        }
        // 회원 - DB에서 끌어오기
        else {
            //id로 고치고픔
            String memberLoginId = memberInfoResponse.getMemberLoginId();
            //db에서 멤버 카트 정보 가져오기
            //주소
            model.addAttribute("memberAddressList", addressService.getAddresses(request));
            model.addAttribute("memberInfo", memberInfoResponse);
        }

        //배송비 정책 끌어오기 - 현재 사용중인 ture인거(api요청)

        // 이 세션에서 attribute의 cartId 알아낸 다음 redis에서 cartId로 상품 정보 가져오기(아이디랑 수량만 받으면 안되나?)
//        GuestCartInfoResponse guestCartInfoResponse = orderService.createGusetOrderSheet(session);

        // 비회원 장바구니 정보 넣어주기
//        model.addAttribute("guestCartInfo", guestCartInfoResponse);

        // 리스트 형식으로 레디스의 상품 정보들 들어감.
        model.addAttribute("guestCartInfo", cartItems);

        // 포장지 가져오기
        List<PackagingGetResponse> packagingList = packagingService.getAllCustomerPackaging();
        model.addAttribute("packagingList", packagingList);

        return "order/orderSheet";
    }

    // 바로 주문
    @GetMapping("/order-now")
    public String orderNow(HttpServletRequest request, @RequestParam Long bookId, @RequestParam int quantity, Model model) {
            // 회원인지 비회원인지 확인
            boolean isMember = false;
            MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(request);

            // 회원정보 가져오기 : 회원이면 isMember=true , 주소, 회원정보 모델에 넘김
            if (memberInfoResponse != null) {
                isMember = true;
                //id로 고치고픔..info정보에 필요 저장하려면 필요할듯.
                String memberLoginId = memberInfoResponse.getMemberLoginId();

                model.addAttribute("memberAddressList", addressService.getAddresses(request));
                model.addAttribute("memberInfo", memberInfoResponse);
            }
            model.addAttribute("isMember", isMember);


            // 책 정보 가져오기 - 단일 책
            BookInfoResponse book = bookCommonService.getBookById(bookId);

            // 요청 만들기, 필요한 정보 추가. 수량 등
            BookOrderRequest bookOrderRequest = new BookOrderRequest(
                    book.getBookId(),
                    book.getBookName(),
                    book.getRegularPrice(),
                    book.getSalePrice(),
                    book.getBookDiscount(),
                    book.isPacked(),
                    book.getStock(),
                    book.getBookThumbnailImageUrl(),
                    quantity);
            List<BookOrderRequest> bookLists = new ArrayList<>();
            bookLists.add(bookOrderRequest);
            model.addAttribute("bookLists", bookLists);

            // 포장지 가져오기
            List<PackagingGetResponse> packagingList = packagingService.getAllCustomerPackaging();
            model.addAttribute("packagingList", packagingList);

            // 배달정책 true인거 가져오기 - 배송정책 서비스에 구현필요..

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

        //TODO : orderservice로 주문 저장하는거

        // 임의의 주문 번호 생성해서 model에 넣기


        //TODO : orderservice로

        //TODO : 결제에 필요한 정보 넘기기 !!!
        return "payment/checkout";
        // TODO 이 결제 완료 후에 주문이 저장.
    }

    // 3. 비회원, 회원 바로 주문 구현


    // 4. 비회원, 회원 주문 내역 페이지 구현

}
