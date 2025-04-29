package com.househub.backend.domain.contract.service;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.contract.dto.ContractListResDto;
import com.househub.backend.domain.contract.dto.CreateContractReqDto;
import com.househub.backend.domain.contract.dto.ContractSearchDto;
import com.househub.backend.domain.contract.dto.CreateContractResDto;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.enums.ContractType;
import com.househub.backend.domain.contract.repository.ContractRepository;
import com.househub.backend.domain.contract.service.impl.ContractServiceImpl;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;

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

	@Mock
	private AgentRepository agentRepository;

	private Agent agent;
	private Customer customer;
	private Property property;
	private Contract contract;

	@BeforeEach
	void setUp() {
		agent = Agent.builder()
			.id(1L)
			.status(AgentStatus.ACTIVE)
			.realEstate(RealEstate.builder().id(1L).build())
			.build();

		customer = Customer.builder()
			.id(1L)
			.build();

		property = Property.builder()
			.id(1L)
			.agent(agent)
			.customer(customer)
			.contracts(new ArrayList<>())
			.build();

		contract = Contract.builder()
			.id(1L)
			.agent(agent)
			.customer(customer)
			.property(property)
			.build();
	}
	//
	// @Test
	// @DisplayName("계약 생성 테스트 - 성공")
	// void createContract_Success() {
	// 	// given
	// 	Long propertyId = 1L;
	// 	Long customerId = 1L;
	// 	Long agentId = 1L;
	// 	CreateContractReqDto requestDto = CreateContractReqDto.builder()
	// 		.propertyId(propertyId)
	// 		.customerId(customerId)
	// 		.contractType(ContractType.JEONSE)
	// 		.jeonsePrice(10000000L)
	// 		.contractStatus(ContractStatus.IN_PROGRESS)
	// 		.build();
	// 	Customer buyer = Customer.builder().id(2L).build();
	// 	when(propertyRepository.findById(requestDto.getPropertyId())).thenReturn(Optional.of(property));
	// 	when(customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(buyer));
	// 	when(contractRepository.existsByCustomerAndPropertyAndStatusNot(any(), any(), any())).thenReturn(false);
	// 	when(agentRepository.findByIdAndStatus(agentId, AgentStatus.ACTIVE)).thenReturn(Optional.of(agent));
	// 	when(contractRepository.save(any(Contract.class))).thenReturn(contract);
	//
	// 	// when
	// 	CreateContractResDto response = contractService.createContract(requestDto, agentId);
	//
	// 	// then
	// 	assertThat(response.getId()).isEqualTo(1L);  // 계약 ID가 1L인지 확인
	// 	verify(contractRepository).save(any(Contract.class));  // contractRepository의 save 메서드가 호출되었는지 확인
	// }
	//
	// @Test
	// @DisplayName("이미 존재하는 계약 예외 처리")
	// void createContract_Failure_ContractAlreadyExists() {
	// 	// given
	// 	Long propertyId = 1L;
	// 	Long customerId = 2L; // 기존 고객과 다른 ID 사용
	// 	Long agentId = 1L;
	//
	// 	Customer buyer = Customer.builder().id(customerId).build(); // 계약 고객
	//
	// 	CreateContractReqDto requestDto = CreateContractReqDto.builder()
	// 		.propertyId(propertyId)
	// 		.customerId(customerId)
	// 		.build();
	//
	// 	when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
	// 	when(customerRepository.findById(customerId)).thenReturn(Optional.of(buyer));
	// 	when(contractRepository.existsByCustomerAndPropertyAndStatusNot(
	// 		any(), any(), eq(ContractStatus.COMPLETED))).thenReturn(true);
	//
	// 	// when & then
	// 	assertThatThrownBy(() -> contractService.createContract(requestDto, agentId))
	// 		.isInstanceOf(AlreadyExistsException.class)
	// 		.hasMessageContaining("해당 고객은 본 매물에 대해 진행중인 계약이 존재합니다.");
	// }
	//
	// @Test
	// @DisplayName("계약 조회 테스트 - 성공")
	// void findContracts() {
	// 	// ContractSearchDto 설정
	// 	ContractSearchDto searchDto = ContractSearchDto.builder().build();
	// 	Pageable pageable = PageRequest.of(0, 10);
	// 	Page<Contract> page = new PageImpl<>(List.of(contract), pageable, 1);
	//
	// 	// Mocking contractRepository.findContractsByRealEstateAndFilters
	// 	when(agentRepository.findByIdAndStatus(agent.getId(), AgentStatus.ACTIVE)).thenReturn(Optional.of(agent));
	// 	when(contractRepository.findContractsByAgentAndFilters(
	// 		any(), any(), any(), any(), any(), any())).thenReturn(page);
	//
	// 	// when
	// 	ContractListResDto result = contractService.findContracts(searchDto, pageable, agent.getId());
	//
	// 	// then
	// 	assertThat(result.getContent().get(0).getId()).isEqualTo(1L);  // 반환된 계약 ID가 1L인지 확인
	// 	verify(contractRepository, times(1)).findContractsByAgentAndFilters(
	// 		agent.getRealEstate().getId(),
	// 		searchDto.getAgentName(),
	// 		searchDto.getCustomerName(),
	// 		searchDto.getContractType(),
	// 		searchDto.getStatus(),
	// 		pageable
	// 	);  // contractRepository.findContractsByRealEstateAndFilters 정확하게 호출되었는지 검증
	// }
	//
	// @Test
	// @DisplayName("계약 수정 테스트 - 성공")
	// void updateContract() {
	// 	// given
	// 	CreateContractReqDto requestDto = mock(CreateContractReqDto.class);
	// 	Contract contract = mock(Contract.class);
	// 	Customer buyer = Customer.builder().id(2L).build();
	// 	when(contractRepository.findById(anyLong())).thenReturn(Optional.of(contract));
	// 	when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
	// 	when(customerRepository.findById(anyLong())).thenReturn(Optional.of(buyer));
	//
	// 	// when(requestDto.getContractStatus()).thenReturn(ContractStatus.IN_PROGRESS);
	// 	// when(contractRepository.existsByCustomerAndPropertyAndStatusNot(any(), any(), any())).thenReturn(false);
	//
	// 	// when
	// 	contractService.updateContract(1L, requestDto);
	//
	// 	// then
	// 	verify(contract).updateContract(requestDto);
	// }
	//
	// @Test
	// @DisplayName("계약 삭제 테스트 - 성공")
	// void deleteContract() {
	// 	// given - 테스트에 필요한 mock 객체들과 상황 설정
	// 	Long contractId = 1L;
	//
	// 	// 계약과 관련된 mock 객체 생성
	// 	Contract mockContract = mock(Contract.class);
	// 	Property mockProperty = mock(Property.class);
	//
	// 	// 진행 중인 다른 계약 mock
	// 	Contract inProgressContract = mock(Contract.class);
	//
	// 	// 계약 ID로 계약을 찾으면 mockContract를 반환
	// 	when(contractRepository.findById(contractId)).thenReturn(Optional.of(mockContract));
	//
	// 	// 해당 계약에 연결된 매물을 반환
	// 	when(mockContract.getProperty()).thenReturn(mockProperty);
	//
	// 	// 매물이 가지고 있는 계약 목록에는 완료된 계약(mockContract)와 진행 중 계약(inProgressContract)이 있음
	// 	when(mockProperty.getContracts()).thenReturn(List.of(mockContract, inProgressContract));
	//
	// 	// mockContract는 완료 상태
	// 	when(mockContract.getStatus()).thenReturn(ContractStatus.COMPLETED);
	//
	// 	// 진행 중 계약은 실제로 진행 중
	// 	when(inProgressContract.getStatus()).thenReturn(ContractStatus.IN_PROGRESS);
	//
	// 	// when - 실제로 삭제 메서드 실행
	// 	contractService.deleteContract(contractId);
	//
	// 	// then - 기대하는 동작이 실제로 발생했는지 검증
	// 	// 계약의 deleteContract() 메서드가 호출되었는지 확인
	// 	verify(mockContract).deleteContract();
	//
	// 	// 진행 중 계약이 있으므로, 매물의 활성 상태는 false
	// 	verify(mockProperty).changeActiveStatus(false);
	// }
	//
	// @Test
	// @DisplayName("존재하지 않는 계약 삭제 시 예외 발생")
	// void deleteNonExistentContract() {
	// 	// given
	// 	when(contractRepository.findById(anyLong())).thenReturn(Optional.empty());
	//
	// 	// when, then
	// 	assertThatThrownBy(() -> contractService.deleteContract(1L))
	// 		.isInstanceOf(ResourceNotFoundException.class)
	// 		.hasMessageContaining("존재하지 않는 계약입니다.");
	// }

}
