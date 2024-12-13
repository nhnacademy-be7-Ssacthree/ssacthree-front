package com.nhnacademy.mini_dooray.ssacthree_front.payment.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.*;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
@ExtendWith(SpringExtension.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private PaymentService paymentService;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        OrderFormRequest orderFormRequest = new OrderFormRequest(
                "John Doe",         // buyerName
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
        );        session.setAttribute("orderFormRequest", orderFormRequest);

        List<BookOrderRequest> bookLists = new ArrayList<>();
        Long bookId = 1L;
        Long packagingId = 2L;

        BookPackagingRequest mockRequest = new BookPackagingRequest(bookId, packagingId);

        BookOrderRequest mockBookOrder = new BookOrderRequest(
                bookId, "Mock Book", 10000, 9000, 10, true, 5, "http://image.url",
                2, null, 500, null
        );
        bookLists.add(mockBookOrder);
        session.setAttribute("bookLists", bookLists);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testConfirmPayment() throws Exception {
        mockMvc.perform(post("/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"paymentKey\": \"testPaymentKey\", \"orderId\": \"testOrderId\", \"amount\": \"10000\" }")
                        .with(csrf())  // CSRF 토큰을 자동으로 포함시킴
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());  // 200 OK 상태 코드 기대
    }


    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void testSuccessPaymentView() throws Exception {
        mockMvc.perform(get("/success"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/success"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void testFailPaymentView() throws Exception {
        mockMvc.perform(get("/fail")
                        .param("code", "400")
                        .param("message", "Payment Failed"))
                .andExpect(status().isOk())
                .andExpect(view().name("payment/fail"))
                .andExpect(model().attribute("code", "400"))
                .andExpect(model().attribute("message", "Payment Failed"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    void testCancelPayment() throws Exception {
        mockMvc.perform(post("/payment/1/cancel")
                        .header(HttpHeaders.REFERER, "/admin/orders")
                        .session(session)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/orders"));
    }

}

