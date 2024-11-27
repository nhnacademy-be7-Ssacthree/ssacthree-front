package com.nhnacademy.mini_dooray.ssacthree_front.order.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import com.nhnacademy.mini_dooray.ssacthree_front.elastic.dto.Paging;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.*;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
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

    // TODO : 주문하는 도서에 패키징 아이디 연결하기
    @PostMapping("/orders/connect-packaging")
    @ResponseBody
    public ResponseEntity<String> connectPackaging(HttpSession httpSession, @RequestBody BookPackagingRequest request) {
        Long bookId = request.getBookId();
        Long packagingId = request.getPackagingId();

        // 세션에서 책 리스트 가져오기
        List<BookOrderRequest> orderDetails =
                (List<BookOrderRequest>) httpSession.getAttribute("bookLists");

        if (orderDetails == null || orderDetails.isEmpty()) {
            return ResponseEntity.badRequest().body("No books found in session.");
        }

        // bookId와 매칭되는 DTO를 찾아 packagingId 추가
        boolean updated = false;
        for (BookOrderRequest detail : orderDetails) {
            if (detail.getBookId().equals(bookId)) { // bookId 매칭 확인
                detail.setPackagingId(packagingId); // packagingId 추가
                updated = true;
                break;
            }
        }

        if (!updated) {
            return ResponseEntity.badRequest().body("Book with the given ID not found.");
        }

        // 세션 업데이트 (선택적으로 필요)
        httpSession.setAttribute("bookLists", orderDetails);

        return ResponseEntity.ok("Packaging ID added successfully.");
    }

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
  
  // 주문 상세 조회 (한 주문의 전체 내용)
  @GetMapping("/orderDetail/{orderId}")
  public String getOrder(@PathVariable("orderId") Long orderId, Model model){
      // 입력받은 주문번호를 orderId로 변환하여 service 요청
      // 1. 조회 시에 order 내역의 customerId 가 멤버인지 확인하고 customerView 또는 memberView 로 나눠서 return 되게(응답 dto 내용은 같음) <- service에서 플래그
      // 2. orderNumber, 이메일가 order 안의 내용과 동일 할 때 조회가 가능하게
      OrderDetailResponse orderDetail = orderService.getOrderDetail(orderId);
      model.addAttribute("orderDetail", orderDetail);

      // order/customerOrderDetail;

      // 정상 처리 시 상세 페이지 반환
      return "order/orderDetail2";
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

    // TODO : 관리자가 주문내역 대기 -> 배송중으로 변경
    // 이때 id는 orderId
    @PostMapping("/admin/orders/{order-id}/delivery-start")
    public String startDelivery(@PathVariable("order-id") String orderId) {
        orderService.changeOrderStatue(orderId);
        return "redirect:/admin/orders";
    }

    // TODO : 관리자가 주문내역 배송중 -> 배송완료로 변경
    @PostMapping("/admin/orders/{order-id}/delivery-complete")
    public String completeDelivery(@PathVariable("order-id") String orderId) {

        return "redirect:/admin/orders";
    }

}
