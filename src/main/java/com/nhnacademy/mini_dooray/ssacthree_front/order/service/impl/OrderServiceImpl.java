package com.nhnacademy.mini_dooray.ssacthree_front.order.service.impl;

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
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.AddressService;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import com.nhnacademy.mini_dooray.ssacthree_front.order.adapter.OrderAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.BookOrderRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderDetailResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderFormRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.*;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderResponseWithCount;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.exception.FailedGetOrderDetail;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import com.nhnacademy.mini_dooray.ssacthree_front.order.utils.OrderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final CartService cartService;
    private final PackagingService packagingService;
    private final MemberService memberService;
    private final AddressService addressService;
    private final BookCommonService bookCommonService;
    private final CustomerService customerService;
    private final DeliveryRuleService deliveryRuleService;
    private final OrderAdapter orderAdapter;

    //
    @Override
    public void prepareOrderCart(HttpServletRequest request, Model model) {
        // 회원인지 아닌지 체크
        boolean isMember = false;
        String accessToken = cartService.getAccessToken(request);

        if (!accessToken.equals("")) {
            MemberInfoResponse memberInfo = memberService.getMemberInfo(request);
            isMember = true;
            model.addAttribute("memberAddressList", addressService.getAddresses(request));
            model.addAttribute("memberInfo", memberInfo);
        }
        model.addAttribute("isMember", isMember);

        // 상품 정보 가져오기
        List<BookOrderRequest> bookLists = buildBookOrderRequestsFromCart(request);
        model.addAttribute("bookLists", bookLists);

        HttpSession session = request.getSession(false);
        session.setAttribute("bookLists", bookLists);

        // 포장지 정보 가져오기
        List<PackagingGetResponse> packagingList = packagingService.getAllCustomerPackaging();
        model.addAttribute("packagingList", packagingList);

        // 배송 정책 가져오기
        DeliveryRuleGetResponse deliveryRule = deliveryRuleService.getCurrentDeliveryRule();
        //model이랑 session 둘다 넣는게 맞나...
        model.addAttribute("deliveryRule", deliveryRule);
        session.setAttribute("deliveryRuleId", deliveryRule.getDeliveryRuleId());
    }

    @Override
    public void prepareOrderNow(HttpServletRequest request, Long bookId, int quantity, Model model) {
        boolean isMember = false;
        String accessToken = cartService.getAccessToken(request);

        if (!accessToken.equals("")) {
            MemberInfoResponse memberInfo = memberService.getMemberInfo(request);
            isMember = true;
            model.addAttribute("memberAddressList", addressService.getAddresses(request));
            model.addAttribute("memberInfo", memberInfo);
        }
        model.addAttribute("isMember", isMember);

        // 단일 상품 정보 가져오기
        BookInfoResponse book = bookCommonService.getBookById(bookId);
        BookOrderRequest bookOrderRequest = buildBookOrderRequest(book, quantity);
        List<BookOrderRequest> bookLists = List.of(bookOrderRequest);
        model.addAttribute("bookLists", bookLists);

        HttpSession session = request.getSession(true);
        session.setAttribute("bookLists", bookLists);

        List<PackagingGetResponse> packagingList = packagingService.getAllCustomerPackaging();
        model.addAttribute("packagingList", packagingList);

        DeliveryRuleGetResponse deliveryRule = deliveryRuleService.getCurrentDeliveryRule();
        model.addAttribute("deliveryRule", deliveryRule);
        session.setAttribute("deliveryRuleId", deliveryRule.getDeliveryRuleId());
    }

    // 결제하기 누르면, 고객 생성 + 결제에 필요한 정보 넘기기
    @Override
    public void processOrder(String memberId, Integer paymentPrice, OrderFormRequest orderFormRequest, HttpSession session, Model model) {
        long customerId = resolveCustomerId(memberId, orderFormRequest);
        orderFormRequest.setCustomerId(customerId);

        session.setAttribute("orderFormRequest", orderFormRequest);

        String orderNumber = OrderUtil.generateOrderNumber();
        model.addAttribute("orderId", orderNumber);
        model.addAttribute("paymentPrice", paymentPrice);

        orderFormRequest.setOrderNumber(orderNumber);
    }

    // 카트 상품 주문서로 가져오기
    private List<BookOrderRequest> buildBookOrderRequestsFromCart(HttpServletRequest request) {
        List<CartItem> cartItems = cartService.initializeCart(request);
        List<BookOrderRequest> bookLists = new ArrayList<>();

        for (CartItem cartItem : cartItems) {
            BookInfoResponse book = bookCommonService.getBookById(cartItem.getId());
            bookLists.add(buildBookOrderRequest(book, cartItem.getQuantity()));
        }
        return bookLists;
    }


    // 멤버 주문내역 조회
    @Override
    public OrderResponseWithCount getOrdersByMemberAndDate(Long customerId, int page, int size, LocalDate startDate, LocalDate endDate) {
        return orderAdapter.getMemberOrders(customerId, page, size, startDate, endDate);
    }

    @Override
    public AdminOrderResponseWithCount adminGetAllOrders(int page, int size, LocalDate startDate, LocalDate endDate) {
        return orderAdapter.adminGetAllOrders(page, size, startDate, endDate);
    }

    @Override
    public OrderDetailResponse getOrderDetail(Long orderId) {
        log.info("주문상세정보를요청합니다.");
        ResponseEntity<OrderDetailResponse> orderAllAttrResponseResponseEntity = orderAdapter.getOrderDetail(orderId);
        if(orderAllAttrResponseResponseEntity.getStatusCode().is2xxSuccessful()){
            OrderDetailResponse orderAllAttrResponse = orderAllAttrResponseResponseEntity.getBody();
            log.info("주문상세정보를받아옴");
            return orderAllAttrResponse;
        }

        throw new FailedGetOrderDetail("조회 실패");
    }

    // 주문내역의 배송 상태를 변경합니다.
    @Override
    public void changeOrderStatue(String orderId) {
        // 대기 -> 배송중, 이때 바꿔야하는 상태를 넣어줘서 작업할 수도 있을듯.
        orderAdapter.changeOrderStatus(orderId);
    }

    @Override
    public OrderDetailResponse getOrderDetailByOrderNumber(String orderNumber,String phoneNumber) {
        log.info("주문번호로 주문 상세 정보를 요청합니다.");
        ResponseEntity<OrderDetailResponse> orderAllAttrResponseResponseEntity = orderAdapter.getOrderDetailByOrderNumber(orderNumber, phoneNumber);
        if(orderAllAttrResponseResponseEntity.getStatusCode().is2xxSuccessful()){
            OrderDetailResponse orderAllAttrResponse = orderAllAttrResponseResponseEntity.getBody();
            log.info("주문 상세 정보를 받아옴");
            return orderAllAttrResponse;
        }

        throw new FailedGetOrderDetail("조회 실패");
    }


    // 책 정보 만들기
    private BookOrderRequest buildBookOrderRequest(BookInfoResponse book, int quantity) {
        int point = (int) (book.getRegularPrice() * 5 * 0.01);
        return new BookOrderRequest(
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
    }

    // 비회원 - 고객 생성하기
    private long resolveCustomerId(String memberId, OrderFormRequest orderFormRequest) {
        if (memberId.isEmpty()) {
            CustomerCreateRequest customerCreateRequest = new CustomerCreateRequest(
                    orderFormRequest.getBuyerName(),
                    orderFormRequest.getBuyerPhone(),
                    orderFormRequest.getBuyerEmail()
            );
            return customerService.createCustomer(customerCreateRequest);
        } else {
            return Long.parseLong(memberId);
        }
    }

    // DB에 주문(모든 정보) 저장 요청
    @Override
    public OrderResponse createOrder(OrderSaveRequest orderSaveRequest) {
        ResponseEntity<OrderResponse> response = orderAdapter.createOrder(orderSaveRequest);
        if (response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        }
        throw new RuntimeException("주문 생성 에러");
    }
}
