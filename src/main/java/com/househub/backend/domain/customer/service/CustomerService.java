package com.househub.backend.domain.customer.service;

import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    Customer createCustomer(CreateCustomerReqDto request);

    Customer findByIdAndDeletedAtIsNull(Long id);

    Customer updateCustomer(Long id, CreateCustomerReqDto reqDto);

    Customer deleteCustomer(Long id);

    List<Customer> findAllByDeletedAtIsNull();
}
