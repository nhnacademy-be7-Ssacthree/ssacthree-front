package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.exception.exception.ValidationFailedException;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberRegisterRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

class MemberRegisterControllerTest {

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberRegisterController memberRegisterController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberRegisterController).build();
    }

    @Test
    void testMemberRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
            .andExpect(view().name("memberRegister"));
    }

    @Test
    void testMemberRegisterSuccess() throws Exception {
        // 각 필드의 유효성을 만족하는 값으로 초기화
        MemberRegisterRequest request = new MemberRegisterRequest("userId", "password", "username",
            "01012341234", "user@example.com", "20000101");

        mockMvc.perform(post("/register")
                .flashAttr("memberRegisterRequest", request))
            .andExpect(redirectedUrl("/"));

        verify(memberService, times(1)).memberRegister(request);
    }


    @Test
    void testMemberRegisterValidationFailed() throws Exception {
        MemberRegisterRequest request = new MemberRegisterRequest(); // 유효하지 않은 데이터 포함
        BindingResult bindingResult = mock(BindingResult.class);
        RedirectAttributes redirectAttributes = mock(RedirectAttributes.class);
        Model model = mock(Model.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(
            List.of(new ObjectError("memberRegisterRequest", "Validation failed"))
        );

        try {
            memberRegisterController.memberRegister(request, bindingResult, model,
                redirectAttributes);
        } catch (ValidationFailedException ex) {
            // ValidationFailedException이 발생했는지 확인
        }

        verify(memberService, never()).memberRegister(any());
    }
}

