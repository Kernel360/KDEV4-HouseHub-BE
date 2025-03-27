package com.househub.backend.domain.contract.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.contract.dto.ContractReqDto;
import com.househub.backend.domain.contract.dto.CreateContractResDto;
import com.househub.backend.domain.contract.dto.FindContractResDto;
import com.househub.backend.domain.contract.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/properties/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    // 계약 등록
    @PostMapping
    public ResponseEntity<SuccessResponse<CreateContractResDto>> createContract(@RequestBody @Valid ContractReqDto contractReqDto) {
        CreateContractResDto response = contractService.createContract(contractReqDto);
        return ResponseEntity.ok(SuccessResponse.success("계약이 성공적으로 등록되었습니다.", "CREATE_CONTRACT_SUCCESS", response));
    }

    // 계약 수정
    @PutMapping("/{contractId}")
    public ResponseEntity<SuccessResponse<Void>> updateContract(
            @PathVariable("contractId") Long contractId,
            @RequestBody @Valid ContractReqDto updatePropertyReqDto
    ) {
        contractService.updateContract(contractId, updatePropertyReqDto);
        return ResponseEntity.ok(SuccessResponse.success("계약이 성공적으로 수정되었습니다.", "UPDATE_CONTRACT_SUCCESS", null));
    }

    // 전체 계약 조회
    @GetMapping
    public ResponseEntity<SuccessResponse<List<FindContractResDto>>> findContracts(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        List<FindContractResDto> response = contractService.findContracts(page, size);
        return ResponseEntity.ok(SuccessResponse.success("계약 조회 성공", "FIND_CONTRACTS_SUCCESS", response));
    }

    // 계약 삭제
    @DeleteMapping("/{contractId}")
    public ResponseEntity<SuccessResponse<Void>> deleteContract(@PathVariable("contractId") Long contractId) {
        contractService.deleteContract(contractId);
        return ResponseEntity.ok(SuccessResponse.success("계약이 성공적으로 삭제되었습니다.", "DELETE_CONTRACT_SUCCESS", null));
    }
}
