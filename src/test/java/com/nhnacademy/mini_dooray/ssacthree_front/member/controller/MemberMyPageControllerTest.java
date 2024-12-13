package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.dto.response.BookListResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.bookset.book.service.BookCommonService;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberInfoUpdateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class MemberMyPageControllerTest {

    @Mock
    private MemberService memberService;

    @Mock
    private BookCommonService bookCommonService;

    @InjectMocks
    private MemberMyPageController memberMyPageController;

    private MockMvc mockMvc;

    private MemberInfoUpdateRequest validRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberMyPageController).build();
        validRequest = new MemberInfoUpdateRequest();
        validRequest.setCustomerPhoneNumber("01036220514");
    }

    @Test
    void testMyPage() throws Exception {
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse();
        when(memberService.getMemberInfo(any(HttpServletRequest.class))).thenReturn(
            memberInfoResponse);

        mockMvc.perform(get("/members/my-page"))
            .andExpect(view().name("myPage"))
            .andExpect(model().attribute("memberInfo", memberInfoResponse));

        verify(memberService, times(1)).getMemberInfo(any(HttpServletRequest.class));
    }


    @Test
    void testUpdateUser_Success() throws Exception {
        // 유효한 값 설정
        mockMvc.perform(post("/members/my-page/update")
                .flashAttr("memberInfoUpdateRequest", validRequest))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/members/my-page"));

    }

    @Test
    void testUpdateUser_PhoneNumberFormatting() throws Exception {
        // 비포맷된 휴대폰 번호 설정
        String rawPhoneNumber = "01012345678";
        String formattedPhoneNumber = "010-1234-5678";

        // POST 요청 시 필요한 모든 필드 설정
        mockMvc.perform(post("/members/my-page/update")
                    .param("customerPhoneNumber", rawPhoneNumber)
                    .param("customerName", "홍길동")
                    .param("customerEmail", "hong@example.com")

                // 컨트롤러에서 필요한 다른 모든 필드들도 여기에 추가
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/members/my-page"));

        // ArgumentCaptor를 사용하여 memberService에 전달된 인자를 캡처
        ArgumentCaptor<MemberInfoUpdateRequest> captor = ArgumentCaptor.forClass(
            MemberInfoUpdateRequest.class);
        verify(memberService, times(1)).memberInfoUpdate(captor.capture(),
            any(HttpServletRequest.class));

        // 캡처된 인자에서 휴대폰 번호가 올바르게 포맷되었는지 검증
        MemberInfoUpdateRequest capturedRequest = captor.getValue();
        assertEquals(formattedPhoneNumber, capturedRequest.getCustomerPhoneNumber());
    }

    @Test
    void testUpdateUser_ValidationError() throws Exception {
        // 유효하지 않은 요청 객체 생성
        MemberInfoUpdateRequest invalidRequest = new MemberInfoUpdateRequest();
        invalidRequest.setCustomerPhoneNumber("123"); // 잘못된 전화번호 형식

        mockMvc.perform(post("/members/my-page/update")
                .flashAttr("memberInfoUpdateRequest", invalidRequest))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/members/my-page"))
            .andExpect(flash().attributeExists("error"));

        // 서비스 호출이 이루어지지 않았음을 확인
        verify(memberService, times(0)).memberInfoUpdate(any(), any());
    }


    @Test
    void testWithdraw() throws Exception {
        mockMvc.perform(post("/members/withdraw"))
            .andExpect(redirectedUrl("/"));

        verify(memberService, times(1)).memberWithdraw(any(HttpServletRequest.class), any(
            HttpServletResponse.class));
    }


    @Test
    void testShowMyLikeBooks_Success() throws Exception {
        // Arrange
        int page = 0;
        int size = 12;
        String[] sort = {"bookName:asc"};

        List<Long> likeBooks = Arrays.asList(1L, 2L, 3L);
        when(bookCommonService.getLikedBooksIdForCurrentUser()).thenReturn(likeBooks);

        List<BookListResponse> bookList = new ArrayList<>();
        BookListResponse book1 = new BookListResponse();
        // 필요한 필드 설정
        bookList.add(book1);
        Page<BookListResponse> books = new PageImpl<>(bookList);

        when(bookCommonService.getBooksByMemberId(page, size, sort)).thenReturn(books);

        // Act & Assert
        mockMvc.perform(get("/members/my-page/likes")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort)
                .cookie(new Cookie("access-token", "valid-token"))
            )
            .andExpect(status().isOk())
            .andExpect(view().name("myLikes"))
            .andExpect(model().attribute("likeBooks", likeBooks))
            .andExpect(model().attribute("books", books))
            .andExpect(model().attribute("baseUrl", "/members/my-page/likes"))
            .andExpect(model().attribute("extraParams", ""))
            .andExpect(model().attribute("sort", sort[0]));

        // Verify
        verify(bookCommonService, times(1)).getLikedBooksIdForCurrentUser();
        verify(bookCommonService, times(1)).getBooksByMemberId(page, size, sort);
    }


}