package com.househub.backend.domain.customer.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.common.exception.InvalidExcelValueException;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.customer.dto.CustomerListResDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerExecutor;
import com.househub.backend.domain.customer.service.CustomerReader;
import com.househub.backend.domain.customer.service.CustomerService;
import com.househub.backend.domain.customer.service.CustomerStore;
import com.househub.backend.domain.customer.util.CustomerExcelProcessor;
import com.househub.backend.domain.customer.util.ExcelParserUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

	private final CustomerStore customerStore;
	private final CustomerReader customerReader;
	private final CustomerExecutor customerExecutor;
	private final CustomerExcelProcessor excelProcessor;

	@Transactional
	public CreateCustomerResDto create(CreateCustomerReqDto request, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		customerReader.checkDuplicatedByContact(request.getContact(), agent.getId());
		Customer storedCustomer = customerStore.create(request.toEntity(agent));
		return CreateCustomerResDto.fromEntity(storedCustomer);
	}

	@Transactional
	public List<CreateCustomerResDto> createAllByExcel(MultipartFile file, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		ExcelParserUtils.ExcelParseResult<CreateCustomerReqDto> result = excelProcessor.process(file);

		if (!result.errors().isEmpty()) {
			throw new InvalidExcelValueException("입력값에 "+ result.errors().size() +" 개의 오류가 존재합니다.", result.errors(), "VALIDATION_ERROR");
		}

		// null 값 필터링
		List<CreateCustomerReqDto> filteredDtos = result.successData().stream()
			.filter(Objects::nonNull)
			.collect(Collectors.toList());

		checkExcelDuplicates(filteredDtos);
		checkDatabaseDuplicates(filteredDtos, agent.getId());

		List<Customer> customers = filteredDtos.stream()
			.map(dto -> dto.toEntity(agent))
			.toList();

		return customerStore.createAll(customers).stream().map(CreateCustomerResDto::fromEntity).toList();
	}


	private void checkExcelDuplicates(List<CreateCustomerReqDto> dtos) {
		Set<String> contacts = new HashSet<>();
		for (CreateCustomerReqDto dto : dtos) {
			if (!contacts.add(dto.getContact())) {
				throw new InvalidExcelValueException("엑셀 내 중복 연락처가 존재합니다. " + dto.getContact(),"DUPLICATED_CONTACT");
			}
		}
	}

	private void checkDatabaseDuplicates(List<CreateCustomerReqDto> dtos, Long agentId) {
		for (CreateCustomerReqDto dto : dtos) {
			customerReader.checkDuplicatedByContact(dto.getContact(), agentId);
			customerReader.checkDuplicatedByEmail(dto.getEmail(),agentId);
		}
	}

	public CustomerListResDto findAll(String keyword, AgentResDto agentDto, Pageable pageable) {
		Agent agent = agentDto.toEntity();
		Page<Customer> customerPage = customerReader.findAllByKeyword(keyword, agent.getId(), pageable);
		Page<CreateCustomerResDto> response = customerPage.map(CreateCustomerResDto::fromEntity);
		return CustomerListResDto.fromPage(response);
	}

	public CreateCustomerResDto findById(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Customer customer = customerReader.findByIdOrThrow(id, agent.getId());
		return CreateCustomerResDto.fromEntity(customer);
	}

	@Transactional
	public CreateCustomerResDto update(Long id, CreateCustomerReqDto request, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Customer updatedCustomer = customerExecutor.validateAndUpdate(id, request, agent);
		return CreateCustomerResDto.fromEntity(updatedCustomer);
	}

	@Transactional
	public CreateCustomerResDto delete(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Customer deletedCustomer = customerExecutor.validateAndDelete(id,agent);
		return CreateCustomerResDto.fromEntity(deletedCustomer);
	}
}
