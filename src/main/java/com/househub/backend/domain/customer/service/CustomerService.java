package com.househub.backend.domain.customer.service;

import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.customer.entity.Customer;

import java.util.List;

public interface CustomerService {

    CreateCustomerResDto createCustomer(CreateCustomerReqDto request);

    CreateCustomerResDto findByIdAndDeletedAtIsNull(Long id);

    CreateCustomerResDto updateCustomer(Long id, CreateCustomerReqDto reqDto);

    CreateCustomerResDto deleteCustomer(Long id);

    List<CreateCustomerResDto> findAllByDeletedAtIsNull();
}
