package com.nhnacademy.mini_dooray.ssacthree_front.payment.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.*;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.dto.PaymentCancelRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.dto.PaymentRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class PaymentController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    @RequestMapping(value = "/confirm")
    public ResponseEntity<JSONObject> confirmPayment(HttpServletRequest request, @RequestBody String jsonBody) throws Exception {

        JSONParser parser = new JSONParser();
        String orderId;
        String amount;
        String paymentKey;
        try {
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            orderId = (String) requestData.get("orderId");
            amount = (String) requestData.get("amount");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        };
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        // 결제 승인 API
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);

        // 결제 성공
        HttpSession session = request.getSession(false);
        OrderFormRequest orderFormRequest = (OrderFormRequest) session.getAttribute("orderFormRequest");
        List<BookOrderRequest> bookLists = (List<BookOrderRequest>) session.getAttribute("bookLists");

        List<OrderDetailSaveRequest> orderDetailList = new ArrayList<>();

        for (BookOrderRequest bookOrder : bookLists) {
            OrderDetailSaveRequest orderDetail = new OrderDetailSaveRequest(
                    bookOrder.getBookId(),
                    1L, //임시
                    bookOrder.getMemberCouponId(),
                    bookOrder.getQuantity(),
                    bookOrder.getRegularPrice(),
                    bookOrder.getPackagingId()
            );

            orderDetailList.add(orderDetail);
            }

        OrderSaveRequest orderSaveRequest = new OrderSaveRequest(
                orderDetailList,
                orderFormRequest.getCustomerId(),
                orderFormRequest.getBuyerName(),
                orderFormRequest.getBuyerEmail(),
                orderFormRequest.getBuyerPhone(),
                orderFormRequest.getRecipientName(),
                orderFormRequest.getRecipientPhone(),
                orderFormRequest.getPostalCode(),
                orderFormRequest.getRoadAddress(),
                orderFormRequest.getDetailAddress(),
                orderFormRequest.getOrderRequest(),
                orderFormRequest.getDeliveryDate(),
                orderFormRequest.getPointToUse(),
                orderFormRequest.getPointToSave(),
                Integer.parseInt(amount),
                (Long) session.getAttribute("deliveryRuleId"),
                orderFormRequest.getOrderNumber()
                );

        // 모든 정보 전달 - 주문상세+포장정보, 포인트 기록 정보, 결제 정보
        OrderResponse order = orderService.createOrder(orderSaveRequest);
        Long dbOrderId = order.getOrderId();

        String approvedAt = (String) jsonObject.get("approvedAt");
        String method = (String) jsonObject.get("method");
        String status = (String) jsonObject.get("status"); // 결제 처리 상태
        PaymentRequest paymentRequest = new PaymentRequest(
                dbOrderId,
                1L,
                Integer.parseInt(amount),
                status,
                paymentKey,
                approvedAt);

        paymentService.savePayment(paymentRequest);
        responseStream.close();

        return ResponseEntity.status(code).body(jsonObject);
    }

    /**
     * 인증성공처리
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String paymentRequest(HttpServletRequest request, Model model) throws Exception {
        return "payment/success";
    }

    /**
     * 인증실패처리
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fail", method = RequestMethod.GET)
    public String failPayment(HttpServletRequest request, Model model) throws Exception {
        String failCode = request.getParameter("code");
        String failMessage = request.getParameter("message");

        model.addAttribute("code", failCode);
        model.addAttribute("message", failMessage);

        return "payment/fail";
    }

    /**
     * 결제 취소
     *
     * @param orderId
     * @param paymentCancelRequest
     * @param request
     * @return
     */
    @PostMapping("/payment/{order-id}/cancel")
    public String cancelPayment(@PathVariable(name = "order-id") Long orderId,
                                @ModelAttribute PaymentCancelRequest paymentCancelRequest,
                                HttpServletRequest request) {
        paymentService.cancelPayment(orderId, paymentCancelRequest);

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/admin/orders");
    }
}
