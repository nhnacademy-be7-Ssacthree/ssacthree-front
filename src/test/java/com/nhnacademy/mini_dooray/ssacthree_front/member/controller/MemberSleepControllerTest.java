package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nhnacademy.mini_dooray.ssacthree_front.commons.dto.MessageResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.MemberSleepToActiveRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ExtendWith(MockitoExtension.class)
class MemberSleepControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberSleepController memberSleepController;

    @BeforeEach
    void setup() {

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");

        mockMvc = MockMvcBuilders.standaloneSetup(memberSleepController)
            .setViewResolvers(viewResolver)
            .build();
    }

    @Test
    void testSleepMemberChangeActive_Success() throws Exception {
        
        String certNumber = "123456";
        String memberLoginId = "testuser";

        MessageResponse messageResponse = new MessageResponse("Member activated successfully");

        when(memberService.memberSleepToActive(any(MemberSleepToActiveRequest.class)))
            .thenReturn(messageResponse);

        mockMvc.perform(post("/sleep-member")
                .param("certNumber", certNumber)
                .param("memberLoginId", memberLoginId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"))
            .andExpect(flash().attribute("memberActiveMessage", "Member activated successfully"));
    }

}
