package com.househub.backend.domain.contract.service;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.contract.dto.ContractReqDto;
import com.househub.backend.domain.contract.dto.CreateContractResDto;
import com.househub.backend.domain.contract.dto.FindContractResDto;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.repository.ContractRepository;
import com.househub.backend.domain.contract.service.impl.ContractServiceImpl;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {
    @InjectMocks
    private ContractServiceImpl contractService;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private CustomerRepository customerRepository;

    private Property property;
    private Customer seller;
    private Customer buyer;
    private Agent agent;
    private ContractReqDto reqDto;

    @Test// completed
    @DisplayName("계약 생성 테스트")
    void createContract_Success() {
        // given
        Long propertyId = 1L;
        Long customerId = 1L;
        ContractReqDto requestDto = ContractReqDto.builder()
                .propertyId(propertyId)
                .customerId(customerId)
                .build();

        Property property = mock(Property.class);
        Customer customer = mock(Customer.class);
        Contract contract = mock(Contract.class);

        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(contractRepository.existsByCustomerAndPropertyAndStatusNot(any(), any(), any())).thenReturn(false);
        when(contractRepository.save(any(Contract.class))).thenReturn(contract);
        when(contract.getContractId()).thenReturn(1L);

        // when
        CreateContractResDto response = contractService.createContract(requestDto);

        // then
        assertThat(response.getContractId()).isEqualTo(1L);  // 계약 ID가 1L인지 확인
        verify(contractRepository).save(any(Contract.class));  // contractRepository의 save 메서드가 호출되었는지 확인
    }

    @Test// completed
    @DisplayName("이미 존재하는 계약 예외 처리")
    void createContract_Failure_ContractAlreadyExists() {
        // given
        Long propertyId = 1L;
        Long customerId = 1L;
        ContractReqDto requestDto = ContractReqDto.builder()
                .propertyId(propertyId)
                .customerId(customerId)
                .build();

        Property property = mock(Property.class);
        Customer customer = mock(Customer.class);
        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(contractRepository.existsByCustomerAndPropertyAndStatusNot(customer, property, ContractStatus.COMPLETED)).thenReturn(true);  // 이미 완료되지 않은 계약이 있음

        // when & then
        assertThatThrownBy(() -> contractService.createContract(requestDto))
                .isInstanceOf(AlreadyExistsException.class)
                .hasMessageContaining("해당 고객은 본 매물에 대해 완료되지 않은 계약이 존재합니다.");
    }

    private Contract createMockContract(Long id) {
        Contract contract = Contract.builder().contractId(id).build();
        // 계약 객체의 ID 설정 (필요에 따라 추가 속성 설정 가능)
        return contract;
    }

    @Test
    @DisplayName("계약 조회 테스트")
    void findContracts() {
        int page = 0;
        int size = 3;
        Pageable pageable = PageRequest.of(page, size);

        // 모의 DTO 객체 생성
        List<FindContractResDto> mockDtos = List.of(
                mock(FindContractResDto.class),
                mock(FindContractResDto.class),
                mock(FindContractResDto.class)
        );
        // 모의 계약 엔티티 생성
        List<Contract> sampleContracts = Arrays.asList(
                createMockContract(1L),
                createMockContract(2L),
                createMockContract(3L)
        );

        // 모의 페이지 객체 생성 (샘플 계약 데이터 포함)
        Page<Contract> mockPage = new PageImpl<>(sampleContracts, pageable, sampleContracts.size());

        // 리포지토리의 findAll 메서드 동작 모킹
        when(contractRepository.findAll(any(Pageable.class))).thenReturn(mockPage);

        try (MockedStatic<FindContractResDto> mockedStatic = mockStatic(FindContractResDto.class)) {
            // toDto 메서드의 반환값을 목 DTO로 지정
            for (int i = 0; i < sampleContracts.size(); i++) {
                Contract c = sampleContracts.get(i);
                mockedStatic.when(() -> FindContractResDto.toDto(c))
                        .thenReturn(mockDtos.get(i));
            }

            // Act
            List<FindContractResDto> result = contractService.findContracts(page, size);

            // Assert
            assertNotNull(result);
            assertEquals(3, result.size(), "DTO로 변환된 개수 확인");

            // 각 목 DTO 검증
            for (FindContractResDto dto : result) {
                assertNotNull(dto, "각 DTO는 null이 아니어야 함");
            }
        }
    }

    @Test// completed
    @DisplayName("계약 수정 테스트")
    void updateContract() {
        // given
        ContractReqDto requestDto = mock(ContractReqDto.class);
        Contract contract = mock(Contract.class);
        Property property = mock(Property.class);
        Customer customer = mock(Customer.class);

        when(contractRepository.findById(anyLong())).thenReturn(Optional.of(contract));
        when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(requestDto.getContractStatus()).thenReturn(ContractStatus.AVAILABLE);
        when(contractRepository.existsByCustomerAndPropertyAndStatusNot(any(), any(), any())).thenReturn(false);

        // when
        contractService.updateContract(1L, requestDto);

        // then
        verify(contract).updateContract(requestDto);
    }

    @Test// completed
    @DisplayName("계약 삭제 테스트 - 성공")
    void deleteContract() {
        // given
        Contract contract = mock(Contract.class); // 가상의 contract 객체 생성
        // findById()가 호출되면 생성한 mock Contract 객체를 반환하도록 설정
        // anyLong()은 어떤 Long 값이 와도 동일하게 동작
        when(contractRepository.findById(anyLong())).thenReturn(Optional.of(contract));

        // when
        contractService.deleteContract(1L);

        // then
        // 메서드가 호출되었는지 검증
        verify(contract).deleteContract();
    }

    @Test// completed
    @DisplayName("존재하지 않는 계약 삭제 시 예외 발생")
    void deleteNonExistentContract() {
        // given
        when(contractRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> contractService.deleteContract(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("존재하지 않는 계약입니다.");
    }

}
