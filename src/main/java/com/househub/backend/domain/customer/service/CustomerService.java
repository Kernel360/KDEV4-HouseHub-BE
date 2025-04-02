package com.househub.backend.domain.customer.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;

public interface CustomerService {

<<<<<<< Updated upstream
    CreateCustomerResDto createCustomer(CreateCustomerReqDto request, Long id);
=======
    CreateCustomerResDto createCustomer(CreateCustomerReqDto request, Long agentId);
>>>>>>> Stashed changes

    CreateCustomerResDto findByIdAndDeletedAtIsNull(Long id, Long agentId);

    CreateCustomerResDto updateCustomer(Long id, CreateCustomerReqDto reqDto, Long agentId);

    CreateCustomerResDto deleteCustomer(Long id, Long agentId);

    List<CreateCustomerResDto> findAllByDeletedAtIsNull(Long agentId);

<<<<<<< Updated upstream
    List<CreateCustomerResDto> createCustomersByExcel(MultipartFile file, Long id);
=======
    List<CreateCustomerResDto> createCustomersByExcel(MultipartFile file, Long agentId);
>>>>>>> Stashed changes
}
