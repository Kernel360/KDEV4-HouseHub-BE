package com.househub.backend.domain.customer.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.customer.dto.CustomerReqDto;
import com.househub.backend.domain.customer.dto.CustomerResDto;
import com.househub.backend.domain.customer.dto.CustomerListResDto;

public interface CustomerService {
    // Command : 명령 요청
    // Criteria : 조회 요청
    // Info : 리턴

    CustomerResDto create(CustomerReqDto request, AgentResDto agentDto);

    List<CustomerResDto> createAllByExcel(MultipartFile file, AgentResDto agentDto);

    CustomerListResDto findAll(String keyword, AgentResDto agentDto, Pageable pageable, boolean includeDeleted);

    CustomerListResDto findAllRecent(AgentResDto agentDto, Pageable pageable);

    CustomerResDto findDetailsById(Long id, AgentResDto agentDto);

    CustomerResDto update(Long id, CustomerReqDto request, AgentResDto agentDto);

    CustomerResDto delete(Long id, AgentResDto agentDto);

    CustomerResDto restore(Long id, AgentResDto agentDto);
}
