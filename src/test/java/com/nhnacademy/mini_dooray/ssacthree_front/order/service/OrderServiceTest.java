package com.nhnacademy.mini_dooray.ssacthree_front.order.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

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
import com.nhnacademy.mini_dooray.ssacthree_front.order.controller.OrderController;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.*;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.impl.OrderServiceImpl;
import com.nhnacademy.mini_dooray.ssacthree_front.order.utils.OrderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(OrderService.class)
class OrderServiceTest {

  @InjectMocks
  private OrderServiceImpl orderService;

  @Mock
  private CartService cartService;

  @Mock
  private PackagingService packagingService;

  @Mock
  private MemberService memberService;

  @Mock
  private AddressService addressService;

  @Mock
  private BookCommonService bookCommonService;

  @Mock
  private CustomerService customerService;

  @Mock
  private DeliveryRuleService deliveryRuleService;

  @Mock
  private OrderAdapter orderAdapter;

  @Mock
  private HttpServletRequest request;

  @Mock
  private HttpSession session;

  @Mock
  private Model model;

  @Test
  void testPrepareOrderCart() {
    // Arrange
    String mockAccessToken = "mockAccessToken";
    MemberInfoResponse mockMemberInfo = new MemberInfoResponse(
        1L, "mockLoginId", "mockName", "01012345678", "mockEmail",
        "1990-01-01", 100L, "GOLD", 1.5f
    );
    List<CartItem> mockCartItems = List.of(new CartItem(1L, "Book1", 2, 10000, "url"));
    BookInfoResponse mockBookInfo = BookInfoResponse.builder()
        .bookId(1L)
        .bookName("Book1")
        .bookIndex("Index1")
        .bookInfo("Info1")
        .bookIsbn("ISBN1")
        .publicationDate(LocalDateTime.now())
        .regularPrice(10000)
        .salePrice(9000)
        .isPacked(true)
        .stock(10)
        .bookThumbnailImageUrl("url")
        .bookViewCount(0)
        .bookDiscount(10)
        .build();
    BookOrderRequest expectedBookOrderRequest = new BookOrderRequest(
        1L, "Book1", 10000, 9000, 10, true, 10, "url", 2, null, 500, null
    );
    List<BookOrderRequest> expectedBookOrderRequests = List.of(expectedBookOrderRequest);

    List<PackagingGetResponse> mockPackagingList = List.of();
    DeliveryRuleGetResponse mockDeliveryRule = new DeliveryRuleGetResponse(
        1L, "Standard", 3000, 0, true, LocalDateTime.now()
    );

    HttpSession mockSession = mock(HttpSession.class);

    when(request.getSession(false)).thenReturn(mockSession);
    when(cartService.getAccessToken(request)).thenReturn(mockAccessToken);
    when(memberService.getMemberInfo(request)).thenReturn(mockMemberInfo);
    when(addressService.getAddresses(request)).thenReturn(List.of());
    when(cartService.initializeCart(request)).thenReturn(mockCartItems);
    when(bookCommonService.getBookById(1L)).thenReturn(mockBookInfo);
    when(packagingService.getAllCustomerPackaging()).thenReturn(mockPackagingList);
    when(deliveryRuleService.getCurrentDeliveryRule()).thenReturn(mockDeliveryRule);

    // Act
    orderService.prepareOrderCart(request, model);

    // Assert
    verify(mockSession).setAttribute(eq("bookLists"), argThat(argument -> {
      List<BookOrderRequest> actualList = (List<BookOrderRequest>) argument;
      return actualList.equals(expectedBookOrderRequests);
    }));
    verify(mockSession).setAttribute(eq("deliveryRuleId"), eq(1L));
    verify(model).addAttribute(eq("isMember"), eq(true));
    verify(model).addAttribute(eq("memberInfo"), eq(mockMemberInfo));
    verify(model).addAttribute(eq("bookLists"), argThat(argument -> {
      List<BookOrderRequest> actualList = (List<BookOrderRequest>) argument;
      return actualList.equals(expectedBookOrderRequests);
    }));
    verify(model).addAttribute(eq("packagingList"), eq(mockPackagingList));
    verify(model).addAttribute(eq("deliveryRule"), eq(mockDeliveryRule));
  }


  @Test
  void testPrepareOrderNow() {
    // Arrange
    String mockAccessToken = "mockAccessToken"; // AccessToken 설정
    MemberInfoResponse mockMemberInfo = new MemberInfoResponse(
        1L, "mockLoginId", "mockName", "01012345678", "mockEmail",
        "1990-01-01", 100L, "GOLD", 1.5f
    );
    BookInfoResponse mockBookInfo = BookInfoResponse.builder()
        .bookId(1L)
        .bookName("Book1")
        .bookIndex("Index1")
        .bookInfo("Info1")
        .bookIsbn("ISBN1")
        .publicationDate(LocalDateTime.now())
        .regularPrice(10000)
        .salePrice(9000)
        .isPacked(true)
        .stock(10)
        .bookThumbnailImageUrl("url")
        .bookViewCount(0)
        .bookDiscount(10)
        .build();
    List<PackagingGetResponse> mockPackagingList = List.of();
    DeliveryRuleGetResponse mockDeliveryRule = new DeliveryRuleGetResponse(
        1L, "Standard", 3000, 0, true, LocalDateTime.now()
    );

    HttpSession mockSession = mock(HttpSession.class);

    // Mock 설정
    when(cartService.getAccessToken(request)).thenReturn(mockAccessToken); // AccessToken 반환
    when(memberService.getMemberInfo(request)).thenReturn(mockMemberInfo);
    when(bookCommonService.getBookById(1L)).thenReturn(mockBookInfo);
    when(packagingService.getAllCustomerPackaging()).thenReturn(mockPackagingList);
    when(deliveryRuleService.getCurrentDeliveryRule()).thenReturn(mockDeliveryRule);
    when(request.getSession(true)).thenReturn(mockSession);

    // Act
    orderService.prepareOrderNow(request, 1L, 2, model);

    // Assert
    verify(model).addAttribute(eq("isMember"), eq(true));
    verify(model).addAttribute(eq("memberInfo"), eq(mockMemberInfo));
    verify(model).addAttribute(eq("bookLists"), argThat(argument -> {
      List<BookOrderRequest> actualList = (List<BookOrderRequest>) argument;
      return actualList.size() == 1 && actualList.get(0).getBookId().equals(1L);
    }));
    verify(model).addAttribute(eq("packagingList"), eq(mockPackagingList));
    verify(model).addAttribute(eq("deliveryRule"), eq(mockDeliveryRule));
    verify(mockSession).setAttribute(eq("bookLists"), anyList());
    verify(mockSession).setAttribute(eq("deliveryRuleId"), eq(1L));
  }


  @Test
  void testGetOrderDetailByOrderNumber() {
    // Arrange
    String orderNumber = "ORD12345";
    String phoneNumber = "01012345678";
    OrderDetailResponse mockResponse = new OrderDetailResponse();
    when(orderAdapter.getOrderDetailByOrderNumber(eq(orderNumber), eq(phoneNumber)))
        .thenReturn(ResponseEntity.ok(mockResponse));

    // Act
    OrderDetailResponse response = orderService.getOrderDetailByOrderNumber(orderNumber, phoneNumber);

    // Assert
    assertNotNull(response);
    assertEquals(mockResponse, response);
    verify(orderAdapter).getOrderDetailByOrderNumber(eq(orderNumber), eq(phoneNumber));
  }

  @Test
  void testCreateOrder() {
    // Arrange
    OrderSaveRequest mockRequest = new OrderSaveRequest();
    OrderResponse mockResponse = new OrderResponse();
    when(orderAdapter.createOrder(eq(mockRequest))).thenReturn(ResponseEntity.ok(mockResponse));

    // Act
    OrderResponse response = orderService.createOrder(mockRequest);

    // Assert
    assertNotNull(response);
    assertEquals(mockResponse, response);
    verify(orderAdapter).createOrder(eq(mockRequest));
  }

  @Test
  void testAdminGetAllOrders() {
    // Arrange
    int page = 0;
    int size = 10;
    LocalDateTime startDate = LocalDateTime.now().minusMonths(1);
    LocalDateTime endDate = LocalDateTime.now();
    AdminOrderResponseWithCount mockResponse = new AdminOrderResponseWithCount(List.of(), 5);

    when(orderAdapter.adminGetAllOrders(eq(page), eq(size), eq(startDate.toLocalDate()), eq(endDate.toLocalDate())))
        .thenReturn(mockResponse);

    // Act
    AdminOrderResponseWithCount response = orderService.adminGetAllOrders(page, size, startDate.toLocalDate(), endDate.toLocalDate());

    // Assert
    assertNotNull(response);
    assertEquals(5, response.getTotalOrders());
    verify(orderAdapter).adminGetAllOrders(eq(page), eq(size), eq(startDate.toLocalDate()), eq(endDate.toLocalDate()));
  }


  @Test
  void testGetOrdersByMemberAndDate() {
    // Arrange
    Long customerId = 1L;
    int page = 0;
    int size = 10;
    LocalDate startDate = LocalDate.of(2023, 12, 1);
    LocalDate endDate = LocalDate.of(2023, 12, 31);

    List<OrderListResponse> mockOrders = List.of(
        new OrderListResponse(1L, LocalDate.of(2023, 12, 10), 50000, "DELIVERED"),
        new OrderListResponse(2L, LocalDate.of(2023, 12, 15), 75000, "SHIPPED")
    );
    OrderResponseWithCount mockResponse = new OrderResponseWithCount(mockOrders, 2);

    // Mock 설정
    when(orderAdapter.getMemberOrders(customerId, page, size, startDate, endDate)).thenReturn(mockResponse);

    // Act
    OrderResponseWithCount result = orderService.getOrdersByMemberAndDate(customerId, page, size, startDate, endDate);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.getTotalOrders());
    assertEquals(mockOrders, result.getOrders());
    verify(orderAdapter, times(1)).getMemberOrders(customerId, page, size, startDate, endDate);
  }


  @Test
  void testGetOrderDetail() {
    // Arrange
    Long orderId = 1L;
    OrderDetailResponse mockResponse = new OrderDetailResponse(
        orderId,
        LocalDate.of(2023, 12, 10),
        "ORD12345",
        LocalDate.of(2023, 12, 15),
        "INV123",
        "John Doe",
        "01012345678",
        "Please deliver before 6 PM.",
        "123 Street",
        "Apt 456",
        "98765",
        50000,
        3000,
        List.of(),
        1L,
        LocalDateTime.now(),
        47000,
        "PAY123",
        "COMPLETED",
        "CREDIT_CARD"
    );

    when(orderAdapter.getOrderDetail(orderId)).thenReturn(ResponseEntity.ok(mockResponse));

    // Act
    OrderDetailResponse result = orderService.getOrderDetail(orderId);

    // Assert
    assertNotNull(result);
    assertEquals(orderId, result.getOrderId());
    assertEquals("ORD12345", result.getOrderNumber());
    verify(orderAdapter, times(1)).getOrderDetail(orderId);
  }


  @Test
  void testProcessOrder() {
    // Arrange
    String memberId = ""; // 비회원일 경우
    Integer paymentPrice = 50000; // 결제 금액
    HttpSession mockSession = mock(HttpSession.class); // Mock HttpSession

    OrderFormRequest mockOrderFormRequest = new OrderFormRequest(
        "John Doe", // 구매자 이름
        "john.doe@example.com", // 구매자 이메일
        "01012345678", // 구매자 전화번호
        "Jane Doe", // 수령자 이름
        "01087654321", // 수령자 전화번호
        "12345", // 우편번호
        "123 Road Name", // 도로명 주소
        "Apt 678", // 상세 주소
        "Leave at the door", // 배송 요청사항
        LocalDate.of(2023, 12, 15), // 배송 지정 날짜
        1000, // 사용 포인트
        500, // 적립 포인트
        null, // 총 결제 금액 (추후 설정)
        null, // 고객 ID (추후 설정)
        null // 주문 번호 (추후 설정)
    );

    String mockOrderNumber = "ORD12345"; // Mock 주문 번호

    // Mock CustomerService 호출
    when(customerService.createCustomer(any(CustomerCreateRequest.class))).thenReturn(1L);

    // Mock Static 메서드
    try (MockedStatic<OrderUtil> mockedOrderUtil = mockStatic(OrderUtil.class)) {
      mockedOrderUtil.when(OrderUtil::generateOrderNumber).thenReturn(mockOrderNumber);

      // Act
      orderService.processOrder(memberId, paymentPrice, mockOrderFormRequest, mockSession, model);

      // Assert
      assertEquals(1L, mockOrderFormRequest.getCustomerId()); // 고객 ID가 설정되었는지 확인
      assertEquals(mockOrderNumber, mockOrderFormRequest.getOrderNumber()); // 주문 번호가 설정되었는지 확인
      verify(mockSession).setAttribute(eq("orderFormRequest"), eq(mockOrderFormRequest)); // 세션에 저장된 데이터 검증
      verify(model).addAttribute(eq("orderId"), eq(mockOrderNumber)); // 모델에 추가된 주문 번호 검증
      verify(model).addAttribute(eq("paymentPrice"), eq(paymentPrice)); // 모델에 추가된 결제 금액 검증
    }
  }


  @Test
  void testChangeOrderStatue() {
    // Arrange
    Long orderId = 1L;
    String status = "SHIPPED";
    ChangeOrderStatusRequest mockRequest = new ChangeOrderStatusRequest(orderId, status);

    // Act
    orderService.changeOrderStatue(orderId, status);

    // Assert
    verify(orderAdapter, times(1)).changeOrderStatus(refEq(mockRequest));
  }

}