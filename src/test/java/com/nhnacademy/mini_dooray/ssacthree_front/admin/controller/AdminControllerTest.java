package com.nhnacademy.mini_dooray.ssacthree_front.admin.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nhnacademy.mini_dooray.ssacthree_front.admin.dto.AdminLoginRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.admin.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    void testAdmin() throws Exception {
        mockMvc.perform(get("/admin"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/admin"));
    }

    @Test
    void testAdminLogin() throws Exception {
        mockMvc.perform(get("/admin-login"))
            .andExpect(status().isOk())
            .andExpect(view().name("admin/adminLogin"));
    }

    @Test
    void testAdminLoginPost() throws Exception {
        // Arrange
        String username = "admin";
        String password = "password";

        // Act & Assert
        mockMvc.perform(post("/admin-login")
                .param("userName", username) // Adjusted parameter name
                .param("password", password))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/admin"));

        // Verify that adminService.login was called
        ArgumentCaptor<AdminLoginRequest> captor = ArgumentCaptor.forClass(AdminLoginRequest.class);

        verify(adminService, times(1)).login(any(HttpServletResponse.class), captor.capture());
    }


    @Test
    void testAdminLogout() throws Exception {
        mockMvc.perform(post("/admin-logout"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/"));

        // Verify that adminService.logout was called
        verify(adminService, times(1)).logout(any(HttpServletResponse.class));
    }
}
