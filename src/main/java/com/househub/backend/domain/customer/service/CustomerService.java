package com.househub.backend.domain.customer.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.customer.dto.CustomerListResDto;
import com.househub.backend.domain.customer.dto.CustomerSearchDto;

public interface CustomerService {


    CreateCustomerResDto createCustomer(CreateCustomerReqDto request, Long agentId);

    CreateCustomerResDto findByIdAndDeletedAtIsNull(Long id, Long agentId);

    CreateCustomerResDto updateCustomer(Long id, CreateCustomerReqDto reqDto, Long agentId);

    CreateCustomerResDto deleteCustomer(Long id, Long agentId);

    CustomerListResDto findAllByDeletedAtIsNull(CustomerSearchDto searchDto, Long agentId, Pageable pageable);

    List<CreateCustomerResDto> createCustomersByExcel(MultipartFile file, Long agentId);
}
