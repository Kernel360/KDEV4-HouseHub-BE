package com.househub.backend.domain.customer.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.customer.dto.CustomerListResDto;

public interface CustomerService {
    // Command : 명령 요청
    // Criteria : 조회 요청
    // Info : 리턴

    CreateCustomerResDto createCustomer(CreateCustomerReqDto request, AgentResDto agentDto);

    CreateCustomerResDto findById(Long id, AgentResDto agentDto);

    CreateCustomerResDto updateCustomer(Long id, CreateCustomerReqDto reqDto, AgentResDto agentDto);

    CreateCustomerResDto deleteCustomer(Long id, AgentResDto agentDto);

    CustomerListResDto findAll(String searchDto, AgentResDto agentDto, Pageable pageable);

    List<CreateCustomerResDto> createCustomersByExcel(MultipartFile file, AgentResDto agentDto);
}
