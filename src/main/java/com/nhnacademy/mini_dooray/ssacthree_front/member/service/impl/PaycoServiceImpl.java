package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.CookieUtil;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.util.ExceptionParser;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.PaycoIdAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.PaycoTokenAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PaycoGetTokenResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PaycoLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PaycoMemberResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PaycoMemberResponse.Data;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.PaycoMemberResponse.Member;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.LoginFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.PaycoConnectionFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.PaycoService;
import feign.FeignException;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaycoServiceImpl implements PaycoService {

    private final PaycoTokenAdapter paycoTokenAdapter;
    private final PaycoIdAdapter paycoIdAdapter;
    private final MemberAdapter memberAdapter;


    @Value("${payco.authorization_code_url}")
    private String authorizationCodeUrl;

    @Value("${payco.client_id}")
    private String clientId;

    @Value("${payco.response_type}")
    private String responseType;

    @Value("${payco.redirect_url}")
    private String redirectUrl;

    @Value("${payco.redirect_url_for_connection}")
    private String redirectUrlForConnection;

    @Value("${payco.service_provider_code}")
    private String serviceProviderCode;

    @Value("${payco.user_locale}")
    private String userLocale;

    @Value("${payco.grant_type}")
    private String grantType;

    @Value("${payco.client_secret}")
    private String clientSecret;

    @Override
    public String getAuthorizationCodeUrl() {
        StringBuilder params = new StringBuilder("?");
        params.append("client_id=").append(clientId)
            .append("&response_type=").append(responseType)
            .append("&redirect_uri=").append(redirectUrl)
            .append("&serviceProviderCode=").append(serviceProviderCode)
            .append("&userLocale=").append(userLocale);

        return "redirect:" + authorizationCodeUrl + params.toString();
    }

    @Override
    public String getAuthorizationCodeForConnection() {
        StringBuilder params = new StringBuilder("?");
        params.append("client_id=").append(clientId)
            .append("&response_type=").append(responseType)
            .append("&redirect_uri=").append(redirectUrlForConnection)
            .append("&serviceProviderCode=").append(serviceProviderCode)
            .append("&userLocale=").append(userLocale);

        return "redirect:" + authorizationCodeUrl + params.toString();
    }


    @Override
    public String getAccessToken(String code) {

        try {
            ResponseEntity<PaycoGetTokenResponse> response = paycoTokenAdapter.getToken(clientId,
                grantType,
                code,
                clientSecret);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody())
                    .map(body -> body.getAccessToken())
                    .orElseThrow(() -> new IllegalStateException("응답 본문이 null입니다."));
            }
            return null;
        } catch (FeignException e) {
            // 우선은 null로 처리
            return null;
        }
    }

    @Override
    public String getPaycoIdNo(String accessToken) {
        try {
            ResponseEntity<PaycoMemberResponse> response = paycoIdAdapter.getIdNo(clientId,
                accessToken);
            if (response.getStatusCode().is2xxSuccessful()) {
                return Optional.ofNullable(response.getBody())
                    .map(PaycoMemberResponse::getData)
                    .map(Data::getMember)
                    .map(Member::getIdNo)
                    .orElseThrow(() -> new IllegalStateException("응답 본문 또는 데이터가 null입니다."));
            }
            return null;
        } catch (FeignException e) {
            return null;
        }

    }

    @Override
    public String paycoLogin(String paycoIdNo, HttpServletResponse httpServletResponse) {
        try {
            ResponseEntity<String> response = memberAdapter.memberPaycoLogin(
                new PaycoLoginRequest(paycoIdNo));
            if (response.getStatusCode().is2xxSuccessful()) {
                CookieUtil.addCookieFromFeignClient(httpServletResponse, response);
                return response.getBody();
            }
            throw new LoginFailedException("로그인에 실패하였습니다.");
        } catch (FeignException e) {
            throw new LoginFailedException(e.getMessage());
        }
    }


    @Override
    public String paycoConnect(String paycoIdNo) {
        try {
            ResponseEntity<String> response = memberAdapter.memberPaycoConnection(
                new PaycoLoginRequest(paycoIdNo));
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }
            throw new PaycoConnectionFailedException("연동에 실패하였습니다.");
        } catch (FeignException e) {
            throw new PaycoConnectionFailedException(
                ExceptionParser.getErrorMessageFromFeignException(e.contentUTF8()));
        }

    }
}
