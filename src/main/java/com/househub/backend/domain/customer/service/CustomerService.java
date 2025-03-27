package com.househub.backend.domain.customer.service;

import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
public interface CustomerService {

    CreateCustomerResDto createCustomer(CreateCustomerReqDto request);

    CreateCustomerResDto findByIdAndDeletedAtIsNull(Long id);

    CreateCustomerResDto updateCustomer(Long id, CreateCustomerReqDto reqDto);

    CreateCustomerResDto deleteCustomer(Long id);

    List<CreateCustomerResDto> findAllByDeletedAtIsNull();

    List<CreateCustomerResDto> createCustomersByExcel(MultipartFile file);
}
