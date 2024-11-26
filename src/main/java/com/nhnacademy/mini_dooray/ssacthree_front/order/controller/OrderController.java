package com.nhnacademy.mini_dooray.ssacthree_front.order.controller;

import co.elastic.clients.util.DateTime;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.dto.DeliveryRuleGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.delivery_rule.service.DeliveryRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.dto.PackagingGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.packaging.service.PackagingService;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.domain.CartItem;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import com.nhnacademy.mini_dooray.ssacthree_front.customer.dto.CustomerCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.customer.service.CustomerService;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto.Paging;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.AddressService;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.*;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import com.nhnacademy.mini_dooray.ssacthree_front.order.utils.OrderUtil;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class OrderController {

    private final CartService cartService;
    private final MemberService memberService;
    private final OrderService orderService;

    // TODO : 각각의 중복되는거 service로 빼기
    // 비회원, 회원 주문 페이지 이동 -> isMember로 구분해서 뷰 띄우기

    // 1. 장바구니 -> 주문하기
    @GetMapping("/order-cart")
    public String orderCart(HttpServletRequest request, Model model) {
        orderService.prepareOrderCart(request, model);
        // TODO : 카트 비우기
        return "order/orderSheet"; // 주문서 페이지
    }

    // 2. 책 상세 -> 바로 주문하기
    @GetMapping("/order-now")
    public String orderNow(HttpServletRequest request, @RequestParam Long bookId, @RequestParam int quantity, Model model) {
        orderService.prepareOrderNow(request, bookId, quantity, model);
        return "order/orderSheet"; // 주문서 페이지
    }

    // 3. 주문서 -> 결제하기
    @PostMapping("/payment")
    public String order(@ModelAttribute("memberId") String memberId,
                        @RequestParam(name = "paymentPrice") Integer paymentPrice,
                        @ModelAttribute OrderFormRequest orderFormRequest,
                        HttpSession httpSession,
                        Model model) {
        orderService.processOrder(memberId, paymentPrice, orderFormRequest, httpSession, model);
        return "payment/checkout"; // 결제 수단 선택 페이지
    }

    // TODO 결제 후 주문 상세 보여주기

    // TODO 4. 비회원 주문 내역 페이지 구현

  // TODO 5. 회원 주문 내역 페이지 구현
  @GetMapping("/orders")
  public String getOrders(HttpServletRequest request,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
      Model model) {
    // AccessToken을 통해 회원 정보 가져오기
    String accessToken = cartService.getAccessToken(request); // 일단 이거로 기능 씀
    if (accessToken == null || accessToken.isEmpty()) {
      return "redirect:/login"; // 비회원은 로그인 페이지로 리다이렉트
    }

    // 회원 정보 로드
    MemberInfoResponse memberInfo = memberService.getMemberInfo(request);
    if (memberInfo == null) {
      return "redirect:/login"; // 회원 정보가 없으면 로그인 페이지로 리다이렉트
    }

    // 날짜 기본 값 설정
    LocalDate now = LocalDate.now();
    if (startDate == null) {
      startDate = now.minusMonths(3); // 3개월 전
    }
    if (endDate == null) {
      endDate = now; // 오늘
    }

    // 주문 데이터 조회 (DTO 사용)
    OrderResponseWithCount response = orderService.getOrdersByMemberAndDate(
        memberInfo.getCustomerId(), page, size, startDate, endDate);

    // 페이지네이션 정보 생성
    int totalPages = (int) Math.ceil((double) response.getTotalOrders() / size);
    Paging paging = new Paging(page, size, totalPages, null);


    // 모델에 데이터 전달
    model.addAttribute("orders", response.getOrders());
    model.addAttribute("paging", paging);
    model.addAttribute("memberInfo", memberInfo);
    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("extraParams", "startDate=" + startDate + "&endDate=" + endDate);
    model.addAttribute("today", LocalDate.now());

    return "order/orderList"; // 주문 내역 뷰

  }

  // 관리자 주문 내역 보기
    @GetMapping("/admin/orders")
    public String orderListView(HttpServletRequest request,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                Model model) {
        // 날짜 기본 값 설정
        LocalDate now = LocalDate.now();
        if (startDate == null) {
            startDate = now.minusMonths(3); // 3개월 전
        }
        if (endDate == null) {
            endDate = now; // 오늘
        }

        // 주문 데이터 조회 (DTO 사용)
        AdminOrderResponseWithCount response = orderService.adminGetAllOrders(
                page, size, startDate, endDate);

        // 페이지네이션 정보 생성
        int totalPages = (int) Math.ceil((double) response.getTotalOrders() / size);
        Paging paging = new Paging(page, size, totalPages, null);


        // 모델에 데이터 전달
        model.addAttribute("orders", response.getOrders());
        model.addAttribute("paging", paging);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("extraParams", "startDate=" + startDate + "&endDate=" + endDate);
        model.addAttribute("today", LocalDate.now());
        model.addAttribute("baseUrl", "/admin/orders");


        return "admin/orders";
    }



    // TODO 5. 주문 상태 변경 -> 관리자
}
