package com.househub.backend.domain.contract.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.contract.dto.ContractReqDto;
import com.househub.backend.domain.contract.dto.CreateContractResDto;
import com.househub.backend.domain.contract.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<SuccessResponse<Void>> updateProperty(
            @PathVariable("contractId") Long contractId,
            @RequestBody @Valid ContractReqDto updatePropertyReqDto
    ) {
        contractService.updateContract(contractId, updatePropertyReqDto);
        return ResponseEntity.ok(SuccessResponse.success("매물이 성공적으로 수정되었습니다.", "UPDATE_CONTRACT_SUCCESS", null));
    }
}
