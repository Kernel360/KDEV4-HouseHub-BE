package com.househub.backend.domain.customer.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.househub.backend.domain.crawlingProperty.service.TagReader;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.common.exception.InvalidExcelValueException;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.crawlingProperty.entity.Tag;
import com.househub.backend.domain.customer.dto.CustomerReqDto;
import com.househub.backend.domain.customer.dto.CustomerResDto;
import com.househub.backend.domain.customer.dto.CustomerListResDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.entity.CustomerTagMap;
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
	private final TagReader tagReader;

	@Transactional
	public CustomerResDto create(CustomerReqDto request, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		customerReader.checkDuplicatedByContact(request.getContact(), agent.getId());

		Customer customer = request.toEntity(agent);
		List<Tag> tagList = tagReader.findAllByIds(request.getTagIds());
		tagList.forEach(tag->
			customer.getCustomerTagMaps().add(
				CustomerTagMap.builder()
					.customer(customer)
					.tag(tag)
					.build()
			));
		Customer storedCustomer = customerStore.create(customer);

		return CustomerResDto.fromEntity(storedCustomer);
	}

	@Transactional
	public List<CustomerResDto> createAllByExcel(MultipartFile file, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		ExcelParserUtils.ExcelParseResult<CustomerReqDto> result = excelProcessor.process(file);
		List<CustomerReqDto> filteredDtos = new ArrayList<>(result.successData());

		if (!result.errors().isEmpty()) {
			throw new InvalidExcelValueException("입력값에 " + result.errors().size() + " 개의 오류가 존재합니다.", result.errors(),
				"VALIDATION_ERROR");
		}

		checkExcelDuplicates(filteredDtos);
		checkDatabaseDuplicates(filteredDtos, agent.getId());

		List<Customer> customers = filteredDtos.stream()
			.map(dto -> dto.toEntity(agent))
			.toList();

		return customerStore.createAll(customers).stream().map(CustomerResDto::fromEntity).toList();
	}

	private void checkExcelDuplicates(List<CustomerReqDto> dtos) {
		Set<String> contacts = new HashSet<>();
		for (CustomerReqDto dto : dtos) {
			if (!contacts.add(dto.getContact())) {
				throw new InvalidExcelValueException("엑셀 내 중복 연락처가 존재합니다. " + dto.getContact(), "DUPLICATED_CONTACT");
			}
		}
	}

	private void checkDatabaseDuplicates(List<CustomerReqDto> dtos, Long agentId) {
		for (CustomerReqDto dto : dtos) {
			customerReader.checkDuplicatedByContact(dto.getContact(), agentId);
			customerReader.checkDuplicatedByEmail(dto.getEmail(), agentId);
		}
	}

	public CustomerListResDto findAll(String keyword, AgentResDto agentDto, Pageable pageable, boolean includeDeleted) {
		Agent agent = agentDto.toEntity();
		Page<Customer> customerPage = customerReader.findAllByKeyword(keyword, agent.getId(), pageable, includeDeleted);
		Page<CustomerResDto> response = customerPage.map(CustomerResDto::fromEntity);
		return CustomerListResDto.fromPage(response);
	}

	public CustomerResDto findDetailsById(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Customer customer = customerReader.findByIdOrThrow(id, agent.getId());
		return CustomerResDto.fromEntity(customer);
	}

	@Transactional
	public CustomerResDto update(Long id, CustomerReqDto request, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Customer updatedCustomer = customerExecutor.validateAndUpdate(id, request, agent);
		return CustomerResDto.fromEntity(updatedCustomer);
	}

	@Transactional
	public CustomerResDto delete(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Customer deletedCustomer = customerExecutor.validateAndDelete(id, agent);
		return CustomerResDto.fromEntity(deletedCustomer);
	}

	@Transactional
	public CustomerResDto restore(Long id, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		Customer restoredCustomer = customerExecutor.validateAndRestore(id,agent);
		return CustomerResDto.fromEntity(restoredCustomer);
	}
}
