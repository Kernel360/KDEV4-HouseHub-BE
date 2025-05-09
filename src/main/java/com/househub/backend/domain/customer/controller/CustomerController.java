package com.househub.backend.domain.customer.controller;

import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.consultation.dto.ConsultationListResDto;
import com.househub.backend.domain.consultation.service.ConsultationService;
import com.househub.backend.domain.contract.dto.ContractListResDto;
import com.househub.backend.domain.contract.service.ContractService;
import com.househub.backend.domain.customer.dto.CustomerReqDto;
import com.househub.backend.domain.customer.dto.CustomerResDto;
import com.househub.backend.domain.customer.dto.CustomerListResDto;
import com.househub.backend.domain.customer.service.CustomerService;
import com.househub.backend.domain.inquiry.dto.InquiryListResDto;
import com.househub.backend.domain.inquiry.service.InquiryService;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.service.PropertyService;
import com.househub.backend.domain.sms.dto.SmsListResDto;
import com.househub.backend.domain.sms.service.SmsService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;
	private final ConsultationService consultationService;
	private final ContractService contractService;
	private final InquiryService inquiryService;
	private final PropertyService propertyService;
	private final SmsService smsService;

	@Operation(
		summary = "고객 등록",
		description = "새로운 고객을 등록합니다. 이메일이 중복되는 경우 등록이 불가합니다. 로그인한 공인중개사 정보도 함께 저장됩니다."
	)
	@PostMapping("")
	public ResponseEntity<SuccessResponse<CustomerResDto>> createCustomer(
		@Valid @RequestBody CustomerReqDto request) {
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		CustomerResDto response = customerService.create(request,agentDto);
		return ResponseEntity.ok(SuccessResponse.success("고객 등록이 완료되었습니다.", "CUSTOMER_REGISTER_SUCCESS", response));
	}

	// 고객 목록 조회
	@Operation(
		summary = "고객 목록 조회",
		description = "고객 목록을 페이지네이션하여 조회합니다. 키워드 검색이 가능합니다."
	)
	@GetMapping("")
	public ResponseEntity<SuccessResponse<CustomerListResDto>> findAllCustomer(
		@RequestParam(name = "keyword", required = false) String keyword,
		Pageable pageable,
		@RequestParam(name = "includeDeleted", required = false) boolean includeDeleted
	) {
		Pageable adjustedPageable = PageRequest.of(Math.max(pageable.getPageNumber() -1,0),pageable.getPageSize(), pageable.getSort());
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();

		CustomerListResDto response = customerService.findAll(keyword, agentDto, adjustedPageable, includeDeleted);
        return ResponseEntity.ok(SuccessResponse.success("고객 목록 조회에 성공했습니다.", "FIND_ALL_CUSTOMER_SUCCESS", response));
    }

	@GetMapping("/recent")
	public ResponseEntity<SuccessResponse<CustomerListResDto>> getAllRecentCustomers(Pageable pageable) {
		Pageable adjustedPageable = PageRequest.of(Math.max(pageable.getPageNumber() -1,0),pageable.getPageSize(), pageable.getSort());
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		CustomerListResDto response = customerService.findAllRecent(agentDto, adjustedPageable);
		return ResponseEntity.ok(SuccessResponse.success("고객 목록 조회에 성공했습니다.", "FIND_ALL_CUSTOMER_SUCCESS", response));
	}

	@Operation(
		summary = "고객 상세 정보 조회",
		description = "특정 고객의 상세 정보 조회합니다. 삭제된 고객이거나 본인이 등록하지 않은 고객은 조회할 수 없습니다."
	)
	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<CustomerResDto>> findOneCustomer(@PathVariable Long id) {
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		CustomerResDto response = customerService.findDetailsById(id, agentDto);
		return ResponseEntity.ok(SuccessResponse.success("고객 상세 조회가 완료되었습니다.", "FIND_CUSTOMER_SUCCESS", response));
	}

	@Operation(
		summary = "고객 상담 내역 조회",
		description = "특정 고객의 상담 내역을 조회합니다. 삭제된 고객이거나 본인이 등록하지 않은 고객은 조회할 수 없습니다."
	)
	@GetMapping("/{id}/consultations")
	public ResponseEntity<SuccessResponse<ConsultationListResDto>> findCustomerConsultations(@PathVariable Long id,
		Pageable pageable) {
		Pageable adjustedPageable = PageRequest.of(Math.max(pageable.getPageNumber() -1,0),pageable.getPageSize(), pageable.getSort());
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		ConsultationListResDto response = consultationService.findAllByCustomer(id, agentDto.getId(), adjustedPageable);
		return ResponseEntity.ok(SuccessResponse.success("고객 상담 목록 조회가 완료되었습니다.", "FIND_CUSTOMER_CONSULTATIONS_SUCCESS", response));
	}

	@Operation(
		summary = "고객 매수 계약 내역 조회",
		description = "특정 고객의 매수 계약 내역을 조회합니다. 삭제된 고객이거나 본인이 등록하지 않은 고객은 조회할 수 없습니다."
	)
	@GetMapping("/{id}/buy-contracts")
	public ResponseEntity<SuccessResponse<ContractListResDto>> findCustomerBuyContracts(@PathVariable Long id,
		Pageable pageable) {
		Pageable adjustedPageable = PageRequest.of(Math.max(pageable.getPageNumber() -1,0),pageable.getPageSize(), pageable.getSort());
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		ContractListResDto response = contractService.findAllByCustomer(id, adjustedPageable, agentDto.getId());
		return ResponseEntity.ok(SuccessResponse.success("고객 계약 목록 조회가 완료되었습니다.", "FIND_CUSTOMER_CONTRACTS_SUCCESS", response));
	}

	@Operation(
		summary = "고객 매도 계약 내역 조회",
		description = "특정 고객의 매도 계약 내역을 조회합니다. 삭제된 고객이거나 본인이 등록하지 않은 고객은 조회할 수 없습니다."
	)
	@GetMapping("/{id}/sell-contracts")
	public ResponseEntity<SuccessResponse<ContractListResDto>> findCustomerSellContracts(@PathVariable Long id,
		Pageable pageable) {
		Pageable adjustedPageable = PageRequest.of(Math.max(pageable.getPageNumber() -1,0),pageable.getPageSize(), pageable.getSort());
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		List<Property> properties = propertyService.findPropertiesByCustomer(id, adjustedPageable, agentDto.getId());
		ContractListResDto response = contractService.findAllByProperties(properties,adjustedPageable, agentDto.getId());

		return ResponseEntity.ok(SuccessResponse.success("고객 계약 목록 조회가 완료되었습니다.", "FIND_CUSTOMER_CONTRACTS_SUCCESS", response));
	}

	@Operation(
		summary = "고객 문의 내역 조회",
		description = "특정 고객의 문의 내역을 조회합니다. 삭제된 고객이거나 본인이 등록하지 않은 고객은 조회할 수 없습니다."
	)
	@GetMapping("/{id}/inquiries")
	public ResponseEntity<SuccessResponse<InquiryListResDto>> findCustomerInquiries(@PathVariable Long id,
		Pageable pageable) {
		Pageable adjustedPageable = PageRequest.of(Math.max(pageable.getPageNumber() -1,0),pageable.getPageSize(), pageable.getSort());
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		InquiryListResDto response = inquiryService.findAllByCustomer(id, adjustedPageable, agentDto.getId());
		return ResponseEntity.ok(SuccessResponse.success("고객 문의 목록 조회가 완료되었습니다.", "FIND_CUSTOMER_INQUIRIES_SUCCESS", response));
	}

	@Operation(
		summary = "고객 문자 내역 조회",
		description = "특정 고객에게 보낸 문자 내역을 조회합니다. 삭제된 고객이거나 본인이 등록하지 않은 고객은 조회할 수 없습니다."
	)
	@GetMapping("/{id}/sms")
	public ResponseEntity<SuccessResponse<SmsListResDto>> findCustomerSms(@PathVariable Long id, Pageable pageable) {
		Pageable adjustedPageable = PageRequest.of(Math.max(pageable.getPageNumber() -1, 0), pageable.getPageSize(), pageable.getSort());
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		SmsListResDto response = smsService.findAllByCustomer(id, adjustedPageable, agentDto);
		return ResponseEntity.ok(SuccessResponse.success("고객 문자 목록 조회가 완료되었습니다.", "FIND_CUSTOMER_SMS_SUCCESS", response));
	}

	@Operation(
		summary = "고객 정보 수정",
		description = "특정 고객의 정보를 수정합니다. 이메일이 중복되거나 본인이 담당하지 않은 고객은 수정할 수 없습니다."
	)
	@PutMapping("/{id}")
	public ResponseEntity<SuccessResponse<CustomerResDto>> updateCustomer(@PathVariable Long id,
		@Valid @RequestBody CustomerReqDto request) {
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		CustomerResDto response = customerService.update(id, request, agentDto);

        return ResponseEntity.ok(SuccessResponse.success("고객 정보 수정이 완료되었습니다.", "UPDATE_CUSTOMER_SUCCESS", response));
    }

	// 고객 삭제
	@Operation(
		summary = "고객 삭제",
		description = "특정 고객을 삭제합니다. 이미 삭제된 고객은 삭제할 수 없습니다."
	)
	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<CustomerResDto>> deleteCustomer(@PathVariable Long id) {
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		CustomerResDto response = customerService.delete(id,agentDto);

        return ResponseEntity.ok(SuccessResponse.success("해당 고객의 삭제가 완료되었습니다.", "DELETE_CUSTOMER_SUCCESS", response));
    }

	// 고객 복구
	@Operation(
		summary = "고객 복구",
		description = "특정 고객을 복구합니다."
	)
	@PutMapping("/restore/{id}")
	public ResponseEntity<SuccessResponse<CustomerResDto>> restoreCustomer(@PathVariable Long id) {
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		CustomerResDto response = customerService.restore(id,agentDto);

		return ResponseEntity.ok(SuccessResponse.success("해당 고객의 복구가 완료되었습니다.", "RESTORE_CUSTOMER_SUCCESS",response));
	}

	@Operation(
		summary = "고객 정보 엑셀 업로드",
		description = "엑셀 파일을 업로드하여 여러 고객 정보를 한 번에 등록합니다."
	)
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<SuccessResponse<List<CustomerResDto>>> createCustomersByExcel(
		@RequestParam("file") MultipartFile file) {
		AgentResDto agentDto = SecurityUtil.getAuthenticatedAgent();
		if (file.isEmpty()) {
			throw new IllegalArgumentException("업로드할 파일이 없습니다.");
		}
		List<CustomerResDto> response = customerService.createAllByExcel(file,agentDto);
		return ResponseEntity.ok(SuccessResponse.success("고객 정보 등록 완료", "CUSTOMER_REGISTER_SUCCESS", response));
	}

	@Operation(
		summary = "엑셀 템플릿 다운로드",
		description = "고객 정보 등록을 위한 엑셀 템플릿 파일을 다운로드합니다."
	)
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadExcelForm() {
        // 템플릿 파일 로드
        Resource resource = new ClassPathResource("templates/excel_template.xlsx");

        // 파일 이름 설정
        String filename = "excel_template.xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}