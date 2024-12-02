package com.nhnacademy.mini_dooray.ssacthree_front.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.mini_dooray.ssacthree_front.cart.service.CartService;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.*;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;

import java.time.LocalDate;
import java.util.List;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ExtendWith(SpringExtension.class)
class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CartService cartService; // CartService를 Mock으로 주입

  @MockBean
  private MemberService memberService;

  @MockBean
  private OrderService orderService;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    Mockito.when(orderService.getOrderDetailByOrderNumber("invalid-order-number", "000-0000-0000"))
        .thenReturn(null); // 존재하지 않는 주문 번호일 때 null 반환

  }


  @Test
  @WithMockUser(username = "testUser", roles = "USER")  // Simulating a logged-in user
  @DisplayName("주문 조회 화면 테스트")  // Test name in Korean
  void testOrderNumberInputForAuthenticatedUser() throws Exception {
    mockMvc.perform(get("/input"))
        .andExpect(status().isOk())  // Expecting 200 OK response
        .andExpect(view().name("order/orderDetailInput"));  // Expecting the correct view name
  }


  // cart 테스트 코드




  @Test
  @WithMockUser(username = "testUser", roles = {"USER"})
  public void testGetOrderDetailByOrderNumber_NotFound() throws Exception {
    mockMvc.perform(get("/orderDetailbyNum")
            .param("orderNumber", "invalid-order-number")
            .param("phoneNumber", "000-0000-0000"))
        .andExpect(status().isOk()) // 상태 코드 200 확인
        .andExpect(model().attributeDoesNotExist("orderDetail")) // orderDetail 속성이 없음을 확인
        .andExpect(content().string(org.hamcrest.Matchers.containsString("주문 결과를 찾을 수 없습니다."))); // 출력 메시지 확인
  }


  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void testOrder() throws Exception {
    // Arrange
    String memberId = "1";
    Integer paymentPrice = 10000;

    OrderFormRequest orderFormRequest = new OrderFormRequest(
        "John Doe",                   // buyerName
        "john.doe@example.com",       // buyerEmail
        "01012345678",                // buyerPhone
        "Jane Doe",                   // recipientName
        "01087654321",                // recipientPhone
        "12345",                      // postalCode
        "Main Street",                // roadAddress
        "Apt 101",                    // detailAddress
        "Leave at the door",          // orderRequest
        LocalDate.now(),              // deliveryDate
        100,                          // pointToUse
        200,                          // pointToSave
        10000,                        // paymentPrice
        1L,                           // customerId
        "ORDER12345"                  // orderNumber
    );

    MockHttpSession mockSession = new MockHttpSession();

    // Mock OrderService behavior
    doNothing().when(orderService).processOrder(eq(memberId), eq(paymentPrice), any(OrderFormRequest.class), eq(mockSession), any(
        Model.class));

    // Act & Assert
    mockMvc.perform(post("/payment")
            .param("memberId", memberId)
            .param("paymentPrice", String.valueOf(paymentPrice))
            .flashAttr("orderFormRequest", orderFormRequest)
            .session(mockSession)
            .with(csrf())) // CSRF 토큰 추가
        .andExpect(status().isOk())
        .andExpect(view().name("payment/checkout"));

    // Verify interaction
    verify(orderService).processOrder(eq(memberId), eq(paymentPrice), any(OrderFormRequest.class), eq(mockSession), any(Model.class));
  }



  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void testConnectPackaging() throws Exception {
    // Arrange
    Long bookId = 1L;
    Long packagingId = 2L;

    BookPackagingRequest mockRequest = new BookPackagingRequest(bookId, packagingId);

    BookOrderRequest mockBookOrder = new BookOrderRequest(
        bookId, "Mock Book", 10000, 9000, 10, true, 5, "http://image.url",
        2, null, 500, null
    );
    List<BookOrderRequest> mockBookOrderList = List.of(mockBookOrder);

    MockHttpSession mockSession = new MockHttpSession();
    mockSession.setAttribute("bookLists", mockBookOrderList);

    // Act & Assert
    mockMvc.perform(post("/orders/connect-packaging")
            .session(mockSession)
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(mockRequest))
            .with(csrf())) // CSRF 토큰 추가
        .andExpect(status().isOk())
        .andExpect(content().string("포장지 id가 매핑되었습니다."));

    // Verify
    List<BookOrderRequest> updatedBookOrderList = (List<BookOrderRequest>) mockSession.getAttribute("bookLists");
    assertNotNull(updatedBookOrderList);
    assertEquals(packagingId, updatedBookOrderList.get(0).getPackagingId());
  }



  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void testGetOrders() throws Exception {
    // Arrange
    int page = 0;
    int size = 10;
    LocalDate startDate = LocalDate.now().minusMonths(3);
    LocalDate endDate = LocalDate.now();

    MemberInfoResponse mockMemberInfo = new MemberInfoResponse(
        1L, "mockLoginId", "mockName", "01012345678", "mockEmail",
        "1990-01-01", 100L, "GOLD", 1.5f
    );

    // Using OrderListResponse with the correct fields
    List<OrderListResponse> mockOrderList = List.of(new OrderListResponse(
        1L, LocalDate.now(), 5000, "배송 중"
    ));
    OrderResponseWithCount mockResponse = new OrderResponseWithCount(mockOrderList, 1);

    when(cartService.getAccessToken(any(HttpServletRequest.class))).thenReturn("mockAccessToken");
    when(memberService.getMemberInfo(any(HttpServletRequest.class))).thenReturn(mockMemberInfo);
    when(orderService.getOrdersByMemberAndDate(
        eq(mockMemberInfo.getCustomerId()), eq(page), eq(size), eq(startDate), eq(endDate)
    )).thenReturn(mockResponse);

    // Act & Assert
    mockMvc.perform(get("/orders")
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size))
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString()))
        .andExpect(status().isOk())
        .andExpect(view().name("order/orderList"))
        .andExpect(model().attributeExists("orders", "paging", "memberInfo", "startDate", "endDate"))
        .andExpect(model().attribute("orders", mockResponse.getOrders()))
        .andExpect(model().attribute("memberInfo", mockMemberInfo))
        .andExpect(model().attribute("startDate", startDate))
        .andExpect(model().attribute("endDate", endDate));

    // Verify interactions
    verify(cartService).getAccessToken(any(HttpServletRequest.class));
    verify(memberService).getMemberInfo(any(HttpServletRequest.class));
    verify(orderService).getOrdersByMemberAndDate(
        eq(mockMemberInfo.getCustomerId()), eq(page), eq(size), eq(startDate), eq(endDate)
    );
  }

  @Test
  @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
  void testGetOrderDetail() throws Exception {
    // Arrange
    Long orderId = 1L;

    OrderDetailResponse mockOrderDetail = OrderDetailResponse.builder()
        .orderNumber("12345")
        .receiverPhoneNumber("01012345678")
        .build();

    when(orderService.getOrderDetail(orderId)).thenReturn(mockOrderDetail);

    // Act & Assert
    mockMvc.perform(get("/orderDetailbyId/{orderId}", orderId))
        .andExpect(status().isOk()) // 200 상태 코드 기대
        .andExpect(view().name("order/orderDetail"))
        .andExpect(model().attributeExists("orderDetail"))
        .andExpect(model().attribute("orderDetail", mockOrderDetail));

    // Verify interaction
    verify(orderService).getOrderDetail(orderId);
  }


  @Test
  @WithMockUser(username = "testUser", roles = {"USER"})
  void testGetOrderDetailByOrderNumber() throws Exception {
    // Arrange
    String orderNumber = "12345";
    String phoneNumber = "01012345678";

    // Mock OrderDetailResponse 객체 생성
    OrderDetailResponse mockOrderDetail = OrderDetailResponse.builder()
        .orderNumber(orderNumber)
        .receiverPhoneNumber(phoneNumber)
        .build();

    // Service 계층에서 반환할 Mock 설정
    when(orderService.getOrderDetailByOrderNumber(eq(orderNumber), eq(phoneNumber)))
        .thenReturn(mockOrderDetail);

    // Act & Assert
    mockMvc.perform(get("/orderDetailbyNum")
            .param("orderNumber", orderNumber)
            .param("phoneNumber", phoneNumber))
        .andExpect(status().isOk()) // HTTP 200 상태 코드 확인
        .andExpect(view().name("order/orderDetailAllUser")) // 반환된 뷰 이름 확인
        .andExpect(model().attributeExists("orderDetail")) // 모델에 "orderDetail" 속성이 있는지 확인
        .andExpect(model().attribute("orderDetail", mockOrderDetail)); // 모델의 "orderDetail" 값 확인

    // Verify interaction
    verify(orderService).getOrderDetailByOrderNumber(eq(orderNumber), eq(phoneNumber));
  }


  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testOrderListView() throws Exception {
    // Arrange
    int page = 0;
    int size = 10;
    LocalDate startDate = LocalDate.now().minusMonths(3);
    LocalDate endDate = LocalDate.now();

    AdminOrderResponseWithCount mockResponse = new AdminOrderResponseWithCount(
        List.of(new AdminOrderListResponse(1L, LocalDate.now(), 10000, "배송중", "홍길동", "12345", "67890")),
        1
    );

    when(orderService.adminGetAllOrders(page, size, startDate, endDate)).thenReturn(mockResponse);

    // Act & Assert
    mockMvc.perform(get("/admin/orders")
            .param("page", String.valueOf(page))
            .param("size", String.valueOf(size))
            .param("startDate", startDate.toString())
            .param("endDate", endDate.toString()))
        .andExpect(status().isOk()) // 200 상태 코드 기대
        .andExpect(view().name("admin/orders"))
        .andExpect(model().attributeExists("orders", "paging", "startDate", "endDate", "today"))
        .andExpect(model().attribute("orders", mockResponse.getOrders()));

    // Verify
    verify(orderService).adminGetAllOrders(page, size, startDate, endDate);
  }


  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void testStartDelivery() throws Exception {
    // Arrange
    Long orderId = 1L;
    String status = "배송중";
    String refererUrl = "/admin/orders";

    doNothing().when(orderService).changeOrderStatue(orderId, status);

    // Act & Assert
    mockMvc.perform(post("/admin/orders/{order-id}/delivery-status", orderId)
            .param("status", status)
            .header("Referer", refererUrl)
            .with(csrf())) // CSRF 토큰 추가
        .andExpect(status().is3xxRedirection()) // 리다이렉션 확인
        .andExpect(redirectedUrl(refererUrl)); // Referer URL로 리다이렉션 확인

    // Verify interaction
    verify(orderService).changeOrderStatue(orderId, status);
  }

}