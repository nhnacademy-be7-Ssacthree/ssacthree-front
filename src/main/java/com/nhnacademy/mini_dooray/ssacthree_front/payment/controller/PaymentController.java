package com.nhnacademy.mini_dooray.ssacthree_front.payment.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.BookOrderRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderDetailSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderFormRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.dto.OrderSaveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.order.service.OrderService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
            // 클라이언트에서 받은 JSON 요청 바디입니다.
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

        // TODO: 개발자센터에 로그인해서 내 결제위젯 연동 키 > 시크릿 키를 입력하세요. 시크릿 키는 외부에 공개되면 안돼요.
        // @docs https://docs.tosspayments.com/reference/using-api/api-keys
        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
        // @docs https://docs.tosspayments.com/reference/using-api/authorization#%EC%9D%B8%EC%A6%9D
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        // 결제 승인 API를 호출하세요.
        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        // @docs https://docs.tosspayments.com/guides/v2/payment-widget/integration#3-결제-승인하기
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

        // TODO: 결제 성공 및 실패 비즈니스 로직을 구현하세요.
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);

        // TODO : me, jsonObject에서 결제테이블에 필요한거 만들어서 보내기
        String type = (String) jsonObject.get("type"); // 일반결제 등등
        String approvedAt = ((String) jsonObject.get("approvedAt"));
        String method = (String) jsonObject.get("method");
        String status = (String) jsonObject.get("status"); // 결제 처리 상태
        PaymentRequest paymentRequest = new PaymentRequest(paymentKey, orderId, Integer.parseInt(amount), type, method,status, approvedAt);

        // TODO : 결제 성공 ! 주문 저장하기. - 재고차감 등등... shop의 orderService에서 모든 로직 처리하기.
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
                Integer.parseInt(amount),
                (Long) session.getAttribute("deliveryRuleId"),
                orderFormRequest.getOrderNumber()
                );
        // TODO : 주문 정보 저장하기 - 진짜 order저장을 위한.. order랑 컬럼 같아야함
        orderService.createOrder(orderSaveRequest); // 모든 요청들을 여러개 보내거나, saveOrder에 모든 정보 주기.
        // 주문상세+포장정보, 포인트 기록 정보, 결제 정보

        // TODO : 주문 상세 저장하기

        // TODO : 결제정보 저장하기. - 진짜 payment저장을 위한 .. payment랑 컬럼 같게
//        paymentService.savePayment(paymentRequest); // -> 백으로 보내깅


        responseStream.close();

        return ResponseEntity.status(code).body(jsonObject);
    }

    /**
     * 인증성공처리
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String paymentRequest(HttpServletRequest request, Model model) throws Exception {
        // 여기서 저장?


        return "payment/success";
    }

    /**
     * 인증실패처리
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

    // TODO : 결제 취소
}
