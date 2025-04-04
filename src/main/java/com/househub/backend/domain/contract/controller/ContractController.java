package com.househub.backend.domain.contract.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.common.util.SecurityUtil;
import com.househub.backend.domain.contract.dto.ContractReqDto;
import com.househub.backend.domain.contract.dto.ContractSearchDto;
import com.househub.backend.domain.contract.dto.CreateContractResDto;
import com.househub.backend.domain.contract.dto.FindContractResDto;
import com.househub.backend.domain.contract.service.ContractService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
		@RequestBody @Valid ContractReqDto updatePropertyReqDto
	) {
		contractService.updateContract(id, updatePropertyReqDto);
		return ResponseEntity.ok(SuccessResponse.success("계약이 성공적으로 수정되었습니다.", "UPDATE_CONTRACT_SUCCESS", null));
	}

	// 전체 계약 조회
	@GetMapping
	public ResponseEntity<SuccessResponse<List<FindContractResDto>>> findContracts(
		@ModelAttribute ContractSearchDto searchDto,
		Pageable pageable
	) {
		List<FindContractResDto> response = contractService.findContracts(searchDto, pageable, getSignInAgentId());
		return ResponseEntity.ok(SuccessResponse.success("계약 조회 성공", "FIND_CONTRACTS_SUCCESS", response));
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
