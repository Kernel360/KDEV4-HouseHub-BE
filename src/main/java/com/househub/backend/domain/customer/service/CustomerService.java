package com.househub.backend.domain.customer.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;

public interface CustomerService {

    CreateCustomerResDto createCustomer(CreateCustomerReqDto request, Long id);

    CreateCustomerResDto findByIdAndDeletedAtIsNull(Long id);

    CreateCustomerResDto updateCustomer(Long id, CreateCustomerReqDto reqDto);

    CreateCustomerResDto deleteCustomer(Long id);

    List<CreateCustomerResDto> findAllByDeletedAtIsNull();

    List<CreateCustomerResDto> createCustomersByExcel(MultipartFile file, Long id);
}
