package com.househub.backend.domain.contract.service;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.contract.dto.ContractListResDto;
import com.househub.backend.domain.contract.dto.ContractReqDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
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

	@Test
	@DisplayName("계약 생성 테스트 - 성공")
	void createContract_Success() {
		// given
		Long propertyId = 1L;
		Long customerId = 1L;
		Long agentId = 1L;
		ContractReqDto requestDto = ContractReqDto.builder()
			.propertyId(propertyId)
			.customerId(customerId)
			.contractType(ContractType.JEONSE)
			.jeonsePrice(10000000L)
			.contractStatus(ContractStatus.IN_PROGRESS)
			.build();
		Customer buyer = Customer.builder().id(2L).build();
		when(propertyRepository.findById(requestDto.getPropertyId())).thenReturn(Optional.of(property));
		when(customerRepository.findById(requestDto.getCustomerId())).thenReturn(Optional.of(buyer));
		when(contractRepository.existsByCustomerAndPropertyAndStatusNot(any(), any(), any())).thenReturn(false);
		when(agentRepository.findByIdAndStatus(agentId, AgentStatus.ACTIVE)).thenReturn(Optional.of(agent));
		when(contractRepository.save(any(Contract.class))).thenReturn(contract);

		// when
		CreateContractResDto response = contractService.createContract(requestDto, agentId);

		// then
		assertThat(response.getId()).isEqualTo(1L);  // 계약 ID가 1L인지 확인
		verify(contractRepository).save(any(Contract.class));  // contractRepository의 save 메서드가 호출되었는지 확인
	}

	@Test
	@DisplayName("이미 존재하는 계약 예외 처리")
	void createContract_Failure_ContractAlreadyExists() {
		// given
		Long propertyId = 1L;
		Long customerId = 2L; // 기존 고객과 다른 ID 사용
		Long agentId = 1L;

		Customer buyer = Customer.builder().id(customerId).build(); // 계약 고객

		ContractReqDto requestDto = ContractReqDto.builder()
			.propertyId(propertyId)
			.customerId(customerId)
			.build();

		when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(property));
		when(customerRepository.findById(customerId)).thenReturn(Optional.of(buyer));
		when(contractRepository.existsByCustomerAndPropertyAndStatusNot(
			any(), any(), eq(ContractStatus.COMPLETED))).thenReturn(true);

		// when & then
		assertThatThrownBy(() -> contractService.createContract(requestDto, agentId))
			.isInstanceOf(AlreadyExistsException.class)
			.hasMessageContaining("해당 고객은 본 매물에 대해 진행중인 계약이 존재합니다.");
	}

	@Test
	@DisplayName("계약 조회 테스트 - 성공")
	void findContracts() {
		// ContractSearchDto 설정
		ContractSearchDto searchDto = ContractSearchDto.builder().build();
		Pageable pageable = PageRequest.of(0, 10);
		Page<Contract> page = new PageImpl<>(List.of(contract), pageable, 1);

		// Mocking contractRepository.findContractsByRealEstateAndFilters
		when(agentRepository.findByIdAndStatus(agent.getId(), AgentStatus.ACTIVE)).thenReturn(Optional.of(agent));
		when(contractRepository.findContractsByAgentAndFilters(
			any(), any(), any(), any(), any(), any())).thenReturn(page);

		// when
		ContractListResDto result = contractService.findContracts(searchDto, pageable, agent.getId());

		// then
		assertThat(result.getContent().get(0).getId()).isEqualTo(1L);  // 반환된 계약 ID가 1L인지 확인
		verify(contractRepository, times(1)).findContractsByAgentAndFilters(
			agent.getRealEstate().getId(),
			searchDto.getAgentName(),
			searchDto.getCustomerName(),
			searchDto.getContractType(),
			searchDto.getStatus(),
			pageable
		);  // contractRepository.findContractsByRealEstateAndFilters 정확하게 호출되었는지 검증
	}

	@Test
	@DisplayName("계약 수정 테스트 - 성공")
	void updateContract() {
		// given
		ContractReqDto requestDto = mock(ContractReqDto.class);
		Contract contract = mock(Contract.class);
		Customer buyer = Customer.builder().id(2L).build();
		when(contractRepository.findById(anyLong())).thenReturn(Optional.of(contract));
		when(propertyRepository.findById(anyLong())).thenReturn(Optional.of(property));
		when(customerRepository.findById(anyLong())).thenReturn(Optional.of(buyer));

		// when(requestDto.getContractStatus()).thenReturn(ContractStatus.IN_PROGRESS);
		// when(contractRepository.existsByCustomerAndPropertyAndStatusNot(any(), any(), any())).thenReturn(false);

		// when
		contractService.updateContract(1L, requestDto);

		// then
		verify(contract).updateContract(requestDto);
	}

	@Test
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

	@Test
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
