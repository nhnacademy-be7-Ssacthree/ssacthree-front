package com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.domain.CouponEffectivePeriodUnit;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.dto.CouponUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon.service.CouponService;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.coupon_rule.service.CouponRuleService;
import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponService couponService;

    @MockBean
    private CouponRuleService couponRuleService;

    @Test
    @DisplayName("쿠폰 목록 조회")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void getCoupons() throws Exception {
        Mockito.when(couponService.getAllCoupons()).thenReturn(Collections.emptyList());
        Mockito.when(couponRuleService.getAllSelectedCouponRules()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/coupons"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/coupon/coupons"))
                .andExpect(model().attributeExists("coupons", "couponRules"));
    }

    @Test
    @DisplayName("쿠폰 생성 페이지 접근")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void getCreateCouponPage() throws Exception {
        Mockito.when(couponRuleService.getAllSelectedCouponRules()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/admin/coupons/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/coupon/createCoupon"))
                .andExpect(model().attributeExists("couponRules"));
    }

    @Test
    @DisplayName("쿠폰 생성 성공")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void createCoupon() throws Exception {
        // CouponCreateRequest 객체 생성
        CouponCreateRequest request = new CouponCreateRequest();
        request.setCouponName("Test Coupon");
        request.setCouponDescription("Test Coupon Description");
        request.setCouponEffectivePeriod(30);
        request.setCouponEffectivePeriodUnit(CouponEffectivePeriodUnit.DAY);  // Enum 값 설정
        request.setCouponExpiredAt(LocalDateTime.now().plusDays(30));  // 만료일 설정
        request.setCouponRuleId(1L);  // 쿠폰 규칙 ID 설정

        // Mocking 서비스 메서드 호출
        Mockito.when(couponService.createCoupon(any(CouponCreateRequest.class)))
                .thenReturn(null);  // 반환값이 없을 경우 null로 설정

        // CSRF 토큰을 포함한 요청을 보냄
        mockMvc.perform(post("/admin/coupons/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("couponName", "Test Coupon")
                        .param("couponDescription", "Test Coupon Description")
                        .param("couponEffectivePeriod", "30")
                        .param("couponEffectivePeriodUnit", "DAY")  // Enum 값도 전달
                        .param("couponExpiredAt", LocalDateTime.now().plusDays(30).toString())
                        .param("couponRuleId", "1")  // 쿠폰 규칙 ID
                        .with(csrf()))  // CSRF 토큰 포함
                .andExpect(status().is3xxRedirection())  // 리다이렉션 확인
                .andExpect(redirectedUrl("/admin/coupons"));  // 리다이렉션 URL 확인
    }

    @Test
    @DisplayName("쿠폰 생성 실패 - Validation Error")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void createCouponValidationError() throws Exception {
        BindingResult mockBindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(mockBindingResult.getAllErrors()).thenReturn(Collections.emptyList());

        Mockito.doThrow(new ValidationFailedException(mockBindingResult))
                .when(couponService).createCoupon(any(CouponCreateRequest.class));

        mockMvc.perform(post("/admin/coupons/create")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("쿠폰 수정 성공")
    @WithMockUser(username = "testUser", roles = {"ADMIN"})
    void updateCoupon() throws Exception {
        // Mock 데이터 생성
        CouponUpdateRequest request = new CouponUpdateRequest();
        request.setCouponId(1L);

        // Mock Service 동작 정의 (반환값이 있는 경우)
        Mockito.when(couponService.updateCoupon(any(CouponUpdateRequest.class))).thenReturn(null);

        // CSRF 토큰을 포함한 요청을 보냄
        mockMvc.perform(post("/admin/coupons")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("couponId", "1") // CouponUpdateRequest 필드에 맞게 수정
                        .with(csrf())) // CSRF 토큰 포함
                .andExpect(status().is3xxRedirection()) // 리다이렉션 상태 확인
                .andExpect(redirectedUrl("/admin/coupons")); // 리다이렉션 경로 확인
    }

//    @Test
//    @DisplayName("쿠폰 수정 실패 - ValidationException 처리")
//    @WithMockUser(username = "testUser", roles = {"ADMIN"})
//    void updateCouponValidationException() throws Exception {
//        BindingResult mockBindingResult = Mockito.mock(BindingResult.class);
//        Mockito.when(mockBindingResult.getAllErrors()).thenReturn(Collections.emptyList());
//
//        Mockito.doThrow(new ValidationFailedException(mockBindingResult))
//                .when(couponService).updateCoupon(any(CouponUpdateRequest.class));
//
//        mockMvc.perform(post("/admin/coupons")
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("couponId", "1") // Invalid request data for validation
//                        .with(csrf()))
//                .andExpect(status().is4xxClientError());  // Validation error, expecting 400 status
//    }
}