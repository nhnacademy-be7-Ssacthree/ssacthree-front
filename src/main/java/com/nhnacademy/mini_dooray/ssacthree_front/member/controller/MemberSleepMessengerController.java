package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.DoorayAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.Dooray;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.CertNumberService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberSleepMessengerController {

    private final DoorayAdapter doorayAdapter;
    private final CertNumberService certNumberService;


    @PostMapping("/send-cert-number")
    public ResponseEntity<?> sendCertNumber(@RequestBody Map<String, String> payload) {

        String memberLoginId = payload.get("memberLoginId");
        String certNumber = generateCertNumber();
        sendCertNumberToUser(certNumber);
        certNumberService.saveCertNumber(memberLoginId, certNumber);
        // 성공 응답
        return ResponseEntity.ok().build();
    }

    private String generateCertNumber() {
        return String.valueOf((int) (Math.random() * 9000) + 1000); // 4자리 랜덤 숫자
    }

    private void sendCertNumberToUser(String certNumber) {
        doorayAdapter.sendMessage(new Dooray("인증 번호 : " + certNumber));
    }
}
