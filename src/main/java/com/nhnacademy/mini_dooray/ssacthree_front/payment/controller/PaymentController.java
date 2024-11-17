package com.nhnacademy.mini_dooray.ssacthree_front.payment.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private PaymentService paymentService;

    @PostMapping
    public String Payment() {
        //폼정보 request에 담고, 여기에 필요한 customerId등등 폼으로 안받은거 저장하기.

        // 회원주문인지 비회원주문인지 확인
        // 비회원이라면 비회원을 생성한 후 고객id저장(커스터머 저장)'
        // 주문 생성 save

        // 모델에 필요한거 다 넣고 payment로 넘기기

        return "payment/checkout";
    }

    // 결제가 끝나야 주문이 저장됨.

}
