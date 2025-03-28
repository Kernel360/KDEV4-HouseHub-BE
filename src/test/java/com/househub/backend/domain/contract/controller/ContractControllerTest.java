package com.househub.backend.domain.contract.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.househub.backend.common.config.WebSecurityConfig;
import com.househub.backend.domain.contract.dto.ContractReqDto;
import com.househub.backend.domain.contract.dto.CreateContractResDto;
import com.househub.backend.domain.contract.dto.FindContractResDto;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.contract.service.ContractService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ContractController.class)
@Import(WebSecurityConfig.class)
public class ContractControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ContractService contractService;

    @Test
    @DisplayName("계약 생성 API 테스트 - 계약 등록 성공")
    void createContract() throws Exception {
        // given
        ContractReqDto requestDto = ContractReqDto.builder()
                .propertyId(1L)
                .customerId(2L)
                .contractType(ContractType.SALE)
                .contractStatus(ContractStatus.AVAILABLE)
                .salePrice(500000000L)
                .build();

        // when
        CreateContractResDto responseDto = new CreateContractResDto(1L);
        when(contractService.createContract(any(ContractReqDto.class))).thenReturn(responseDto);

        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("계약이 성공적으로 등록되었습니다."))
                .andExpect(jsonPath("$.data.contractId").value(1));
    }

    @Test
    @DisplayName("계약 유효성 검사 - 실패 (거래 가능 상태일 경우 거래 시작일, 만료일 설정 불가)")
    void testInvalidContract() throws Exception {
        ContractReqDto request = ContractReqDto.builder()
                .propertyId(1L)
                .customerId(2L)
                .contractType(ContractType.MONTHLY_RENT)
                .contractStatus(ContractStatus.AVAILABLE)
                .monthlyRentFee(500000)
                .startedAt(LocalDate.now())
                .expiredAt(LocalDate.now().plusYears(1))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("월세 계약 유효성 검사 - 실패 (보증금 없이 월세 금액만 설정)")
    void testInvalidMonthlyRentContract() throws Exception {
        ContractReqDto request = ContractReqDto.builder()
                .propertyId(1L)
                .customerId(2L)
                .contractType(ContractType.MONTHLY_RENT)
                .contractStatus(ContractStatus.IN_PROGRESS)
                .monthlyRentFee(500000)
                .startedAt(LocalDate.now())
                .expiredAt(LocalDate.now().plusYears(1))
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/contracts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("계약 수정 API 테스트 - 계약 수정 성공")
    void updateContract() throws Exception {
        // given
        ContractReqDto requestDto = ContractReqDto.builder()
                .propertyId(1L)
                .customerId(2L)
                .contractType(ContractType.JEONSE)
                .contractStatus(ContractStatus.AVAILABLE)
                .jeonsePrice(200000000L)
                .build();

        // 서비스 호출 시 아무 동작도 하지 않도록 설정
        doNothing().when(contractService).updateContract(anyLong(), any(ContractReqDto.class));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/contracts/{contractId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("계약이 성공적으로 수정되었습니다."))
                .andExpect(jsonPath("$.code").value("UPDATE_CONTRACT_SUCCESS"));
    }

    @Test
    @DisplayName("계약 조회 API 테스트 - 조회 성공")
    void findContracts() throws Exception {
        // given
        List<FindContractResDto> response = Collections.emptyList();
        when(contractService.findContracts(0, 10)).thenReturn(response);

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/contracts")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("계약 조회 성공"))
                .andExpect(jsonPath("$.code").value("FIND_CONTRACTS_SUCCESS"))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @DisplayName("계약 삭제 API 테스트 - 삭제 성공")
    void deleteContract() throws Exception {
        // given
        doNothing().when(contractService).deleteContract(anyLong());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/contracts/{contractId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("계약이 성공적으로 삭제되었습니다."))
                .andExpect(jsonPath("$.code").value("DELETE_CONTRACT_SUCCESS"));
    }
}
