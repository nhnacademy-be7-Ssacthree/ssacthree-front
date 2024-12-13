package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.adapter.CouponAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponGetResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.exception.CouponGetFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CouponServiceImplTest {

    @Mock
    private CouponAdapter couponAdapter;

    @InjectMocks
    private CouponServiceImpl couponService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllCoupons_success() {
        // Given
        CouponGetResponse coupon1 = new CouponGetResponse(1L, "Coupon1", "Description1", 10, null, LocalDateTime.now(), LocalDateTime.now().plusDays(1), 1L);
        CouponGetResponse coupon2 = new CouponGetResponse(2L, "Coupon2", "Description2", 15, null, LocalDateTime.now(), LocalDateTime.now().plusDays(2), 2L);
        List<CouponGetResponse> couponList = Arrays.asList(coupon1, coupon2);
        ResponseEntity<List<CouponGetResponse>> responseEntity = new ResponseEntity<>(couponList, HttpStatus.OK);

        // When
        when(couponAdapter.getAllCoupons()).thenReturn(responseEntity);

        // Then
        List<CouponGetResponse> result = couponService.getAllCoupons();
        assertEquals(2, result.size());
        assertEquals("Coupon1", result.get(0).getCouponName());
        assertEquals("Coupon2", result.get(1).getCouponName());
    }

//    @Test
//    void getAllCoupons_fail_clientError() {
//        // Given
//        HttpClientErrorException clientError = new HttpClientErrorException(HttpStatus.BAD_REQUEST);
//        when(couponAdapter.getAllCoupons()).thenThrow(clientError);
//
//        // Then
//        CouponGetFailedException exception = assertThrows(CouponGetFailedException.class, () -> {
//            couponService.getAllCoupons();
//        });
//        assertEquals("쿠폰 조회에 실패하였습니다.", exception.getMessage());
//    }
//
//    @Test
//    void getAllCoupons_fail_serverError() {
//        // Given
//        HttpServerErrorException serverError = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
//        when(couponAdapter.getAllCoupons()).thenThrow(serverError);
//
//        // When & Then
//        CouponGetFailedException exception = assertThrows(CouponGetFailedException.class, () -> {
//            couponService.getAllCoupons();  // 이 호출에서 catch 블록으로 넘어가 CouponGetFailedException이 던져져야 한다.
//        });
//        assertEquals("쿠폰 조회에 실패하였습니다.", exception.getMessage());
//    }

    @Test
    void createCoupon_success() {
        // Given
        CouponCreateRequest request = new CouponCreateRequest();
        request.setCouponName("NewCoupon");
        request.setCouponDescription("New coupon description");
        request.setCouponEffectivePeriod(30);
        request.setCouponEffectivePeriodUnit(null); // Optional field
        request.setCouponExpiredAt(LocalDateTime.now().plusDays(30));
        request.setCouponRuleId(1L);

        MessageResponse messageResponse = new MessageResponse("쿠폰이 성공적으로 생성되었습니다.");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(messageResponse, HttpStatus.CREATED);

        // When
        when(couponAdapter.createCoupon(any(CouponCreateRequest.class))).thenReturn(responseEntity);

        // Then
        MessageResponse result = couponService.createCoupon(request);
        assertEquals("쿠폰이 성공적으로 생성되었습니다.", result.getMessage());
    }

//    @Test
//    void createCoupon_fail_clientError() {
//        // Given
//        CouponCreateRequest request = new CouponCreateRequest();
//        request.setCouponName("NewCoupon");
//        request.setCouponDescription("New coupon description");
//        request.setCouponEffectivePeriod(30);
//        request.setCouponEffectivePeriodUnit(null);
//        request.setCouponExpiredAt(LocalDateTime.now().plusDays(30));
//        request.setCouponRuleId(1L);
//
//        HttpClientErrorException clientError = new HttpClientErrorException(HttpStatus.BAD_REQUEST);
//        when(couponAdapter.createCoupon(any(CouponCreateRequest.class))).thenThrow(clientError);
//
//        // Then
//        CouponGetFailedException exception = assertThrows(CouponGetFailedException.class, () -> {
//            couponService.createCoupon(request);
//        });
//        assertEquals("쿠폰 생성에 실패하였습니다.", exception.getMessage());
//    }
//
//    @Test
//    void createCoupon_fail_serverError() {
//        // Given
//        CouponCreateRequest request = new CouponCreateRequest();
//        request.setCouponName("NewCoupon");
//        request.setCouponDescription("New coupon description");
//        request.setCouponEffectivePeriod(30);
//        request.setCouponEffectivePeriodUnit(null);
//        request.setCouponExpiredAt(LocalDateTime.now().plusDays(30));
//        request.setCouponRuleId(1L);
//
//        HttpServerErrorException serverError = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
//        when(couponAdapter.createCoupon(any(CouponCreateRequest.class))).thenThrow(serverError);
//
//        // Then
//        CouponGetFailedException exception = assertThrows(CouponGetFailedException.class, () -> {
//            couponService.createCoupon(request);
//        });
//        assertEquals("쿠폰 생성에 실패하였습니다.", exception.getMessage());
//    }

    @Test
    void updateCoupon_success() {
        // Given
        CouponUpdateRequest request = new CouponUpdateRequest();
        request.setCouponId(1L);
        MessageResponse messageResponse = new MessageResponse("쿠폰이 성공적으로 수정되었습니다.");
        ResponseEntity<MessageResponse> responseEntity = new ResponseEntity<>(messageResponse, HttpStatus.OK);

        // When
        when(couponAdapter.updateCoupon(any(CouponUpdateRequest.class))).thenReturn(responseEntity);

        // Then
        MessageResponse result = couponService.updateCoupon(request);
        assertEquals("쿠폰이 성공적으로 수정되었습니다.", result.getMessage());
    }

//    @Test
//    void updateCoupon_fail_clientError() {
//        // Given
//        CouponUpdateRequest request = new CouponUpdateRequest();
//        request.setCouponId(1L);
//        HttpClientErrorException clientError = new HttpClientErrorException(HttpStatus.BAD_REQUEST);
//        when(couponAdapter.updateCoupon(any(CouponUpdateRequest.class))).thenThrow(clientError);
//
//        // Then
//        CouponGetFailedException exception = assertThrows(CouponGetFailedException.class, () -> {
//            couponService.updateCoupon(request);
//        });
//        assertEquals("쿠폰 수정에 실패하였습니다.", exception.getMessage());
//    }
//
//    @Test
//    void updateCoupon_fail_serverError() {
//        // Given
//        CouponUpdateRequest request = new CouponUpdateRequest();
//        request.setCouponId(1L);
//        HttpServerErrorException serverError = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
//        when(couponAdapter.updateCoupon(any(CouponUpdateRequest.class))).thenThrow(serverError);
//
//        // Then
//        CouponGetFailedException exception = assertThrows(CouponGetFailedException.class, () -> {
//            couponService.updateCoupon(request);
//        });
//        assertEquals("쿠폰 수정에 실패하였습니다.", exception.getMessage());
//    }
}
