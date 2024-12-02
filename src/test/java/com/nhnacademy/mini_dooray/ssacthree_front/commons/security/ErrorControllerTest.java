package com.nhnacademy.mini_dooray.ssacthree_front.commons.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ErrorController.class)
@AutoConfigureMockMvc(addFilters = false)
class ErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HttpServletRequest request;


    @Test
    @DisplayName("예외가 없는 경우 - Unauthorized")
    void testHandleUnauthorizedNoException() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/error/unauthorized"))
            .andExpect(status().isOk())
            .andExpect(view().name("error"))
            .andExpect(model().attribute("exception", "인증 실패"));
    }

    @Test
    @DisplayName("예외가 없는 경우 - Forbidden")
    void testHandleForbiddenNoException() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/error/forbidden"))
            .andExpect(status().isOk())
            .andExpect(view().name("error"))
            .andExpect(model().attribute("exception", "권한 없음"));
    }
}
