package com.nhnacademy.mini_dooray.ssacthree_front.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.mini_dooray.ssacthree_front.member.adapter.DoorayAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.CertNumberService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberSleepMessengerController.class)
@AutoConfigureMockMvc(addFilters = false)
class MemberSleepMessengerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoorayAdapter doorayAdapter;

    @MockBean
    private CertNumberService certNumberService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sendCertNumberTest() throws Exception {
        // Arrange: Mock doorayAdapter.sendMessage
        doNothing().when(doorayAdapter).sendMessage(any());

        // Prepare JSON payload
        Map<String, String> payload = new HashMap<>();
        payload.put("memberLoginId", "testUser");

        String jsonPayload = objectMapper.writeValueAsString(payload);

        // Act: Perform POST request
        mockMvc.perform(post("/send-cert-number")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
            .andExpect(status().isOk());

        // Assert: Verify that sendMessage was called with expected arguments
        verify(doorayAdapter).sendMessage(any());

        // Assert: Verify that saveCertNumber was called with correct memberLoginId and any certNumber
        verify(certNumberService).saveCertNumber(eq("testUser"), any(String.class));
    }
}
