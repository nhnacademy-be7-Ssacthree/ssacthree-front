package com.nhnacademy.mini_dooray.ssacthree_front.member.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.MemberAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.AddAddressFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.exception.AddressFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.AddressService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;


@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {

    private final MemberAdapter memberAdapter;

    /**
     *  주소 추가
     * @param request 요청
     * @param addressRequest DTO
     * @return 추가 한 주소를 리턴
     */
    public AddressResponse addNewAddress(HttpServletRequest request, AddressRequest addressRequest) {
        String accessToken = getAccessToken(request);

        try {
            ResponseEntity<AddressResponse> response = memberAdapter.addNewAddress(
                "Bearer " + accessToken, addressRequest);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new AddAddressFailedException("주소 입력에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AddAddressFailedException("주소 입력에 실패하였습니다.");
        }
    }

    /**
     *
     * @param request 요청
     * @return 해당 유저의 주소 리스트 반환
     */
    @Override
    public List<AddressResponse> getAddresses(HttpServletRequest request) {
        String accessToken = getAccessToken(request);

        try {
            ResponseEntity<List<AddressResponse>> response = memberAdapter.getAddresses(
                "Bearer " + accessToken);

            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            }

            throw new AddressFailedException("주소 응답에 실패하였습니다.");
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AddressFailedException("주소 응답에 실패하였습니다.");
        }
    }

    /**
     *
     *  주소 삭제
     * @param addressId 주소 아이디
     * @param request 요청
     */
    @Override
    public void deleteAddress(long addressId, HttpServletRequest request) {
        String accessToken = getAccessToken(request);
        try {
            ResponseEntity<Void> response = memberAdapter.deleteAddress(
                "Bearer " + accessToken,addressId);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new AddressFailedException("주소 삭제에 실패하였습니다.");
            }
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new AddressFailedException("주소 삭제에 실패하였습니다.");
        }

    }

    /**
     *
     * @param request 요청
     * @return 접근 권한 가져오기
     */
    public String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String accessToken = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("access-token")) {
                accessToken = cookie.getValue();
                break;
            }
        }
        return accessToken;
    }
}
