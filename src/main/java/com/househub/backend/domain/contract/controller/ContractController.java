package com.househub.backend.domain.contract.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.contract.dto.CreateContractReqDto;
import com.househub.backend.domain.contract.dto.CreateContractResDto;
import com.househub.backend.domain.contract.service.ContractService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/properties/contracts")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    // 계약 등록
    @PostMapping
    public ResponseEntity<SuccessResponse<CreateContractResDto>> createContract(@RequestBody @Valid CreateContractReqDto createContractReqDto) {
        CreateContractResDto response = contractService.createContract(createContractReqDto);
        return ResponseEntity.ok(SuccessResponse.success("계약이 성공적으로 등록되었습니다.", "CREATE_PROPERTY_SUCCESS", response));
    }
}
