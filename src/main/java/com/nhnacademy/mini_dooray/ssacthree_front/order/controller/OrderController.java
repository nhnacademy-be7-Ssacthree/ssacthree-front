package com.nhnacademy.mini_dooray.ssacthree_front.order.controller;

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
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.BookOrderRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderFormRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponseWithCount;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import com.nhnacademy.mini_dooray.ssacthree_front.order.utils.OrderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping
public class OrderController {

    private final CartService cartService;
    private final PackagingService packagingService;
    private final MemberService memberService;
    private final AddressService addressService;
    private final BookCommonService bookCommonService;
    private final CustomerService customerService;
    private final DeliveryRuleService deliveryRuleService;
    private final OrderService orderService;

    // TODO : 각각의 중복되는거 service로 빼기
    // 비회원, 회원 주문 페이지 이동 -> isMember로 구분해서 뷰 띄우기

    // 1. 장바구니 -> 주문하기
    @GetMapping("/order-cart")
    public String orderCart(HttpServletRequest request, Model model) {
        // 회원인지 비회원인지 확인
        boolean isMember = false;

        // 회원정보 가져오기 : 회원이면 isMember=true , 주소, 회원정보 모델에 넘김.
        // TODO : access token이 있는 경우는 회원
        String accessToken = cartService.getAccessToken(request);

        // TODO : null로 해도 되는지 물어보기 !
        if (!accessToken.equals("")){
            MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(request);
            isMember = true;

            model.addAttribute("memberAddressList", addressService.getAddresses(request));
            model.addAttribute("memberInfo", memberInfoResponse);
        }
        model.addAttribute("isMember", isMember);

        // 상품 정보 가져오기 : 카트정보가져와서 bookinfo끌어온 후 request만들기
        List<CartItem> cartItems = cartService.initializeCart(request);

        // 카트 비었나도 체크해야함.

        // 책 주문 정보 - 카트 기반, 회원 비회원 카트 다시 체크, 지금은 레디스에서 가져오는듯..?
        // TODO : 멤버 카트 정보 어떻게 가져올지 고민하기
        List<BookOrderRequest> bookLists = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            BookInfoResponse book = bookCommonService.getBookById(cartItem.getId());
            int point = (int) (book.getRegularPrice() * 5 * 0.01);
            // 요청 만들기, 필요한 정보 추가. 수량 등
            BookOrderRequest bookOrderRequest = new BookOrderRequest(
                    book.getBookId(),
                    book.getBookName(),
                    book.getRegularPrice(),
                    book.getSalePrice(),
                    book.getBookDiscount(),
                    // TODO : 왜 다 false? -> 수정필요할듯
                    book.isPacked(),
                    book.getStock(),
                    book.getBookThumbnailImageUrl(),
                    cartItem.getQuantity(),
                    null,
                    point,
                    null);
            bookLists.add(bookOrderRequest);
        }
        model.addAttribute("bookLists", bookLists);

        // 포장지 가져오기
        List<PackagingGetResponse> packagingList = packagingService.getAllCustomerPackaging();
        model.addAttribute("packagingList", packagingList);

        // 배달정책 true인거 가져오기 - 배송정책 서비스에 구현필요..
        DeliveryRuleGetResponse deliveryRule = deliveryRuleService.getCurrentDeliveryRule();
        model.addAttribute("deliveryRule", deliveryRule);

        return "order/orderSheet";
    }

    // 2. 책 상세 -> 바로 주문하기
    @GetMapping("/order-now")
    public String orderNow(HttpServletRequest request, @RequestParam Long bookId, @RequestParam int quantity, Model model) {
            boolean isMember = false;
            String accessToken = cartService.getAccessToken(request);

            if(!accessToken.equals("")) {
                MemberInfoResponse memberInfoResponse = memberService.getMemberInfo(request);
                isMember = true;

                model.addAttribute("memberAddressList", addressService.getAddresses(request));
                model.addAttribute("memberInfo", memberInfoResponse);
            }
            model.addAttribute("isMember", isMember);

            // 포인트 적립 정책 가져오기 .. - 지금은 임시로 5%로 그냥 설정


            // 책 정보 가져오기 - 단일 책
            BookInfoResponse book = bookCommonService.getBookById(bookId);
            int point = (int) (book.getRegularPrice() * 5 * 0.01);

            BookOrderRequest bookOrderRequest = new BookOrderRequest(
                    book.getBookId(),
                    book.getBookName(),
                    book.getRegularPrice(),
                    book.getSalePrice(),
                    book.getBookDiscount(),
                    book.isPacked(),
                    book.getStock(),
                    book.getBookThumbnailImageUrl(),
                    quantity,
                    null,
                    point,
                    null
                    );
            List<BookOrderRequest> bookLists = new ArrayList<>();
            bookLists.add(bookOrderRequest);
            model.addAttribute("bookLists", bookLists);

            // 포장지 가져오기
            // TODO : 포장지가 다 false로 들어온다 ..!
            List<PackagingGetResponse> packagingList = packagingService.getAllCustomerPackaging();
            model.addAttribute("packagingList", packagingList);

            // 배달정책 true인거 가져오기 - 배송정책 서비스에 구현필요..
            DeliveryRuleGetResponse deliveryRule = deliveryRuleService.getCurrentDeliveryRule();
            model.addAttribute("deliveryRule", deliveryRule);

            return "order/orderSheet";
    }


    // 3. 주문서 -> 결제하기
    @PostMapping("/payment")
    public String order(@ModelAttribute("memberId") String memberId,
                        @RequestParam(name = "paymentPrice") Integer paymentPrice,
                        @ModelAttribute OrderFormRequest orderFormRequest,
                        HttpSession httpSession,
                        Model model) {
        // 회원이 아니라면 customer만들어서 id저장, 멤버면 바로 전달
        long customerId;
        if (memberId.equals("")) {
            // 비회원 만들기
            CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest(
                    orderFormRequest.getBuyerName(),
                    orderFormRequest.getBuyerPhone(),
                    orderFormRequest.getBuyerEmail()
            );
            customerId = customerService.createCustomer(customerCreateRequest);
        } else {
            // 회원이면
            customerId = Long.parseLong(memberId);
        }
        orderFormRequest.setCustomerId(customerId);

        //TODO : 입력폼에서 넘어온 정보 저장하기 - 세션으로 유지
        // 가주문 만들기.
        httpSession.setAttribute("orderFormRequest", orderFormRequest);

        // 결제에 필요한 정보 넘기기
        String orderNumber = OrderUtil.generateOrderNumber();
        model.addAttribute("orderId", orderNumber);
        model.addAttribute("paymentPrice", paymentPrice);

        // 결제창 넘어가기
        return "payment/checkout";
        // TODO 이 결제 완료 후에 주문이 저장.
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


    return "order/orderList"; // 주문 내역 뷰

  }

    // TODO 5. 주문 상태 변경 -> 관리자

}
