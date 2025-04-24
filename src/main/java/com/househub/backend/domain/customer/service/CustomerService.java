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

    CreateCustomerResDto create(CreateCustomerReqDto request, AgentResDto agentDto);

    List<CreateCustomerResDto> createAllByExcel(MultipartFile file, AgentResDto agentDto);

    CustomerListResDto findAll(String keyword, AgentResDto agentDto, Pageable pageable);

    CreateCustomerResDto findDetailsById(Long id, AgentResDto agentDto);

    CreateCustomerResDto update(Long id, CreateCustomerReqDto request, AgentResDto agentDto);

    CreateCustomerResDto delete(Long id, AgentResDto agentDto);
}
