package com.househub.backend.domain.customer.service.Impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.customer.dto.CustomerListResDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerReader;
import com.househub.backend.domain.customer.service.CustomerService;
import com.househub.backend.domain.customer.service.CustomerStore;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerStore customerStore;
	private final CustomerReader customerReader;

	@Transactional
	public CreateCustomerResDto createCustomer(CreateCustomerReqDto request, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Customer storedCustomer = customerStore.createCustomer(request, agent);
		return CreateCustomerResDto.fromEntity(storedCustomer);
	}

	@Transactional
	public List<CreateCustomerResDto> createCustomersByExcel(MultipartFile file, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		return customerStore.createCustomersByExcel(file, agent)
			.stream()
			.map(CreateCustomerResDto::fromEntity)
			.toList();
	}

	public CustomerListResDto findAll(String keyword, AgentResDto agentDto, Pageable pageable) {
		Agent agent = agentDto.toEntity();
		Page<Customer> customerPage = customerReader.getAllCustomer(keyword, agent.getId(), pageable);
		Page<CreateCustomerResDto> response = customerPage.map(CreateCustomerResDto::fromEntity);
		return CustomerListResDto.fromPage(response);
	}

	public CreateCustomerResDto findById(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Customer customer = customerReader.getCustomerById(id, agent.getId());
		return CreateCustomerResDto.fromEntity(customer);
	}

	@Transactional
	public CreateCustomerResDto updateCustomer(Long id, CreateCustomerReqDto request, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Customer updatedCustomer = customerStore.updateCustomer(id, request, agent);
		return CreateCustomerResDto.fromEntity(updatedCustomer);
	}

	@Transactional
	public CreateCustomerResDto deleteCustomer(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Customer deletedCustomer = customerStore.deleteCustomer(id, agent);
		return CreateCustomerResDto.fromEntity(deletedCustomer);
	}
}
