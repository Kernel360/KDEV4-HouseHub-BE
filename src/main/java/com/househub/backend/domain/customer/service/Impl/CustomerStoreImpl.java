package com.househub.backend.domain.customer.service.Impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.InvalidExcelValueException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.common.response.ErrorResponse;
import com.househub.backend.common.validation.ExcelValidator;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.CustomerReader;
import com.househub.backend.domain.customer.service.CustomerStore;
import com.househub.backend.domain.customer.util.ExcelParserUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerStoreImpl implements CustomerStore {

	private final CustomerRepository customerRepository;
	private final CustomerReader customerReader;

	@Override
	public Customer createCustomer(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public List<Customer> createCustomersByExcel(MultipartFile file, Agent agent) {
		ExcelParserUtils.ExcelParseResult<CreateCustomerReqDto> result = ExcelParserUtils.parseExcel(file, row -> {

			// 1. 검증 로직
			List<ErrorResponse.FieldError> errors = new ExcelValidator().validateRow(row);
			if (!errors.isEmpty()) {
				throw new InvalidExcelValueException("입력값 오류", errors, "VALIDATION_ERROR");
			}

			// 2. DTO 생성
			return CreateCustomerReqDto.builder()
				.name(row.getCell(0).getStringCellValue())
				.ageGroup((int)row.getCell(1).getNumericCellValue())
				.contact(row.getCell(2).getStringCellValue())
				.email(row.getCell(3).getStringCellValue())
				.memo(row.getCell(4).getStringCellValue())
				.gender(Gender.valueOf(row.getCell(5).getStringCellValue()))
				.build();
		});

		if (!result.errors().isEmpty()) {
			throw new InvalidExcelValueException("입력값 오류", result.errors(), "VALIDATION_ERROR");
		}

		return result.successData().stream()
			.map(dto -> dto.toEntity(agent)).map(this::createCustomer)
			.toList();
	}

	@Override
	public Customer updateCustomer(Long id, CreateCustomerReqDto request ,Agent agent) {
		Customer customer = customerRepository.findByIdAndAgentIdAndDeletedAtIsNull(id,agent.getId()).orElseThrow(() -> new ResourceNotFoundException("해당 고객이 존재하지 않습니다:", "CUSTOMER_NOT_FOUND"));

		if (!customer.getContact().equals(request.getContact())) {
			customerRepository.findByContactAndAgentIdAndDeletedAtIsNull(request.getContact(), agent.getId())
				.ifPresent(existingCustomer -> {
					if (!existingCustomer.getId().equals(customer.getId())) {
						throw new AlreadyExistsException(
							"이미 사용 중인 이메일입니다: " + request.getEmail(),
							"EMAIL_ALREADY_EXISTS"
						);
					}
				});
		}
		customer.update(request);

		return customerRepository.save(customer);
	}

	@Override
	public Customer deleteCustomer(Long id, Agent agent) {
		Customer customer = customerRepository.findByIdAndAgentIdAndDeletedAtIsNull(id,agent.getId()).orElseThrow(() -> new ResourceNotFoundException("해당 고객이 존재하지 않습니다:", "CUSTOMER_NOT_FOUND"));
		// 소프트 딜리트
		customer.delete();
		return customerRepository.save(customer);
	}
}

