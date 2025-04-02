package com.househub.backend.domain.customer.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.InvalidExcelValueException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.common.response.ErrorResponse.FieldError;
import com.househub.backend.common.validation.ExcelValidator;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;

    @Transactional
    public CreateCustomerResDto createCustomer(CreateCustomerReqDto request, Long agentId) {
        Agent agent = validateAgent(agentId);
        // 이메일로 고객 조회
        customerRepository.findByEmail(request.getEmail()).ifPresent(customer -> {
            throw new AlreadyExistsException("해당 이메일(" + request.getEmail() + ")로 생성되었던 계정이 이미 존재합니다.", "EMAIL_ALREADY_EXIST");
        });
        Customer customer = request.toEntity(agent);
        // 새로운 고객 생성 및 저장
        return customerRepository.save(customer).toDto();
    }

    @Transactional
    public List<CreateCustomerResDto> createCustomersByExcel(MultipartFile file, Long agentId) {
        List<FieldError> allErrors = new ArrayList<>();
        List<CreateCustomerReqDto> validDtos = new ArrayList<>();
		try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			ExcelValidator validator = new ExcelValidator();

			// 행 단위 검증 및 분류
			sheet.forEach(row -> {
				if (row.getRowNum() == 0)
					return; // 헤더 스킵

				List<FieldError> rowErrors = validator.validateRow(row);
				if (!rowErrors.isEmpty()) {
					allErrors.addAll(rowErrors);
				} else {
					validDtos.add(createCustomerDtoFromRow(row));
				}
			});

			if (!allErrors.isEmpty()) {
				throw new InvalidExcelValueException("입력값을 확인해주세요!", allErrors, "VALIDATION_ERROR");
			}

            // 유효한 데이터만 처리
            return validDtos.stream()
                    .map(request -> createCustomer(request, agentId))
                    .toList();

		} catch (IOException e) {
			throw new RuntimeException("엑셀 처리 실패", e);
		}
	}

	private CreateCustomerReqDto createCustomerDtoFromRow(Row row) {

		return CreateCustomerReqDto.builder()
			.name(row.getCell(0).getStringCellValue())
			.ageGroup((int)row.getCell(1).getNumericCellValue())
			.contact(row.getCell(2).getStringCellValue())
			.email(row.getCell(3).getStringCellValue())
			.memo(row.getCell(4).getStringCellValue())
			.gender(Gender.valueOf(row.getCell(5).getStringCellValue()))
			.build();
	}

    public List<CreateCustomerResDto> findAllByDeletedAtIsNull(Long agentId) {
        Agent agent = validateAgent(agentId);
        return customerRepository.findAllByAgentAndDeletedAtIsNull(agent).stream().map(Customer::toDto).toList();
    }

    public CreateCustomerResDto findByIdAndDeletedAtIsNull(Long id, Long agentId) {
        Agent agent = validateAgent(agentId);
        Customer customer = customerRepository.findByIdAndAgentAndDeletedAtIsNull(id,agent).orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 고객이 존재하지 않습니다:" + id, "CUSTOMER_NOT_FOUND"));
        return customer.toDto();
    }

    @Transactional
    public CreateCustomerResDto updateCustomer(Long id, CreateCustomerReqDto request, Long agentId) {
        Agent agent = validateAgent(agentId);
        customerRepository.findByEmail(request.getEmail()).ifPresent(customer -> {
            throw new AlreadyExistsException("해당 이메일(" + request.getEmail() + ")로 생성되었던 계정이 이미 존재합니다.", "EMAIL_ALREADY_EXIST");
        });
        Customer customer = customerRepository.findByIdAndAgentAndDeletedAtIsNull(id,agent).orElseThrow(() -> new ResourceNotFoundException("해당 고객이 존재하지 않습니다:", "CUSTOMER_NOT_FOUND"));
		customer.update(request);
		return customer.toDto();
	}

    @Transactional
    public CreateCustomerResDto deleteCustomer(Long id, Long agentId) {
        Agent agent = validateAgent(agentId);

        Customer customer = customerRepository.findByIdAndAgentAndDeletedAtIsNull(id,agent).orElseThrow(() -> new ResourceNotFoundException("해당 고객이 존재하지 않습니다:", "CUSTOMER_NOT_FOUND"));
        // 소프트 딜리트
        customer.delete();
        return customer.toDto();
    }

    private Agent validateAgent(Long agentId) {
        return agentRepository.findById(agentId)
            .orElseThrow(() -> new ResourceNotFoundException("공인중개사가 존재하지 않습니다.", "AGENT_NOT_FOUND"));
    }
}
