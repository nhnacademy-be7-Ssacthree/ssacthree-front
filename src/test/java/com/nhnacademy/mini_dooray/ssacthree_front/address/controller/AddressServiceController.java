package com.nhnacademy.mini_dooray.ssacthree_front.address.controller;

import com.nhnacademy.mini_dooray.ssacthree_front.member.controller.MemberAddressController;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.member.dto.AddressResponse;
import com.nhnacademy.mini_dooray.ssacthree_front.member.service.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

class MemberAddressControllerTest {

    @InjectMocks
    private MemberAddressController memberAddressController;

    @Mock
    private AddressService addressService;

    @Mock
    private HttpServletRequest request;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(memberAddressController).build();
    }

    @Test
    void testAddNewAddress() throws Exception {
        AddressRequest addressRequest = new AddressRequest("Home", "123 Street", "Main Road", "12345");

        mockMvc.perform(post("/address")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("addressAlias", addressRequest.getAddressAlias())
                .param("addressDetail", addressRequest.getAddressDetail())
                .param("addressRoadname", addressRequest.getAddressRoadname())
                .param("addressPostalNumber", addressRequest.getAddressPostalNumber()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/address-page"));

        // AddressRequest 객체를 매칭하기 위해 any(AddressRequest.class) 사용
        verify(addressService, times(1)).addNewAddress(any(HttpServletRequest.class), any(AddressRequest.class));
    }

    @Test
    void testAddressPage() throws Exception {
        List<AddressResponse> addressList = List.of(
            new AddressResponse(1L, "Home", "123 Street", "Main Road", "12345"),
            new AddressResponse(2L, "Work", "456 Avenue", "Office Road", "67890")
        );

        when(addressService.getAddresses(any(HttpServletRequest.class))).thenReturn(addressList);

        mockMvc.perform(get("/address-page"))
            .andExpect(status().isOk())
            .andExpect(view().name("address"))
            .andExpect(model().attributeExists("addresses"))
            .andExpect(model().attribute("addresses", addressList));

        verify(addressService, times(1)).getAddresses(any(HttpServletRequest.class));
    }

    @Test
    void testAddressAdd() throws Exception {
        mockMvc.perform(get("/address"))
            .andExpect(status().isOk())
            .andExpect(view().name("addressAdd"));
    }

    @Test
    void testDeleteAddress() throws Exception {
        Long addressId = 1L;

        mockMvc.perform(post("/address/{id}", addressId))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/address-page"));

        verify(addressService, times(1)).deleteAddress(eq(addressId), any(HttpServletRequest.class));
    }
}

