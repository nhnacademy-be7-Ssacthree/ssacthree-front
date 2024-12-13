package com.nhnacademy.mini_dooray.ssacthree_front.customer.service;

import com.nhnacademy.mini_dooray.ssacthree_front.customer.dto.CustomerCreateRequest;

public interface CustomerService {
    Long createCustomer(CustomerCreateRequest request);
}
