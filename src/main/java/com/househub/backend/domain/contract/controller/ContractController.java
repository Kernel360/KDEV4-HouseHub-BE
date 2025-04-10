package com.househub.backend.domain.contract.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.contract.dto.ContractListResDto;
import com.househub.backend.domain.contract.dto.ContractReqDto;
import com.househub.backend.domain.contract.dto.ContractSearchDto;
import com.househub.backend.domain.contract.dto.CreateContractResDto;
import com.househub.backend.domain.contract.dto.FindContractResDto;
import com.househub.backend.domain.contract.service.ContractService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class ContractController {

	private final ContractService contractService;

	// 계약 등록
	@PostMapping
	public ResponseEntity<SuccessResponse<CreateContractResDto>> createContract(
		@RequestBody @Valid ContractReqDto contractReqDto) {
		CreateContractResDto response = contractService.createContract(contractReqDto, getSignInAgentId());
		return ResponseEntity.ok(SuccessResponse.success("계약이 성공적으로 등록되었습니다.", "CREATE_CONTRACT_SUCCESS", response));
	}

	// 계약 수정
	@PutMapping("/{id}")
	public ResponseEntity<SuccessResponse<Void>> updateContract(
		@PathVariable("id") Long id,
		@RequestBody @Valid ContractReqDto reqDto
	) {
		contractService.updateContract(id, reqDto);
		return ResponseEntity.ok(SuccessResponse.success("계약이 성공적으로 수정되었습니다.", "UPDATE_CONTRACT_SUCCESS", null));
	}

	// 전체 계약 조회
	@GetMapping
	public ResponseEntity<SuccessResponse<ContractListResDto>> findContracts(
		@ModelAttribute ContractSearchDto searchDto,
		Pageable pageable
	) {
		// page를 1-based에서 0-based로 변경
		int page = Math.max(pageable.getPageNumber() - 1, 0);
		int size = pageable.getPageSize();
		Pageable adjustedPageable = PageRequest.of(page, size, pageable.getSort());
		ContractListResDto response = contractService.findContracts(searchDto, adjustedPageable, getSignInAgentId());
		return ResponseEntity.ok(SuccessResponse.success("계약 조회 성공", "FIND_CONTRACTS_SUCCESS", response));
	}

	// 계약 상세 조회
	@GetMapping("/{id}")
	public ResponseEntity<SuccessResponse<FindContractResDto>> findContract(@PathVariable("id") Long id) {
		FindContractResDto response = contractService.findContract(id);
		return ResponseEntity.ok(SuccessResponse.success("계약 상세 조회 성공", "FIND_DETAIL_CONTRACT_SUCCESS", response));
	}

	// 계약 삭제
	@DeleteMapping("/{id}")
	public ResponseEntity<SuccessResponse<Void>> deleteContract(@PathVariable("id") Long id) {
		contractService.deleteContract(id);
		return ResponseEntity.ok(SuccessResponse.success("계약이 성공적으로 삭제되었습니다.", "DELETE_CONTRACT_SUCCESS", null));
	}

	/**
	 * 현재 로그인한 에이전트의 ID를 반환합니다.
	 *
	 * @return 현재 로그인한 에이전트의 ID
	 */
	private Long getSignInAgentId() {
		return SecurityUtil.getAuthenticatedAgent().getId();
	}
}
