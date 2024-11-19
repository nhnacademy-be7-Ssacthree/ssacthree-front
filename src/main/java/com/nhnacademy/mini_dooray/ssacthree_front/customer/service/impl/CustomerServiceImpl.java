package com.nhnacademy.mini_dooray.ssacthree_front.customer.service.impl;

import com.nhnacademy.mini_dooray.ssacthree_front.customer.adapter.CustomerAdapter;
import com.nhnacademy.mini_dooray.ssacthree_front.customer.dto.CustomerCreateRequest;
import com.nhnacademy.mini_dooray.ssacthree_front.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerAdapter customerAdapter;

    public Long createCustomer(CustomerCreateRequest request) {
        Long customerId = customerAdapter.createCustomer(request).getBody();
        return customerId;
    }
}
