package com.househub.backend.domain.contract.service.impl;

import java.util.Optional;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.contract.dto.*;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.service.ContractReader;
import com.househub.backend.domain.contract.service.ContractService;
import com.househub.backend.domain.contract.service.ContractStore;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.entity.PropertyCondition;
import com.househub.backend.domain.property.service.PropertyReader;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
	private final CustomerRepository customerRepository;
	private final AgentRepository agentRepository;

	private final PropertyReader propertyReader;
	private final ContractStore contractStore;
	private final ContractReader contractReader;

	/**
	 * 계약 등록
	 * @param dto 해당 매물에 계약 등록하는 DTO
	 * @return 등록한 계약 id 를 반환하는 DTO
	 */
	@Override
	@Transactional
	public CreateContractResDto createContract(ContractReqDto dto, Long agentId) {
		// 계약할 매물 조건 조회
		PropertyCondition propertyCondition = propertyReader.findPropertyConditionBy(dto.getPropertyConditionId());
		// 계약할 매물 조회
		Property property = propertyReader.findPropertyBy(propertyCondition.getProperty().getId());
		// 계약할 고객 조회
		Customer customer = findCustomerById(dto.getCustomerId());
		// 1. 예외 처리
		// 매물을 등록한 고객과 계약할 고객이 동일한 경우 예외 처리
		isSameCustomerAndProperty(customer, property);
		// 2. 예외처리
		// 계약자가 동일 매물에 진행중인 계약이 있으면 등록 불가
		contractReader.existsInProgressContract(property, customer, dto.getContractStatus());


		// dto → entity 변환 후 저장
		Agent agent = findAgentById(agentId);
		Contract contract = dto.toEntity(propertyCondition, customer, agent);
		contract = contractStore.create(contract);
		// 계약 진행중 상태로 등록시 매물 조건 비활성화
		if (dto.getContractStatus() == ContractStatus.IN_PROGRESS) {
			// 해당 매물에 대해 활성화 상태인 매물 조건들 모두 비활성화
			property.getConditions().stream()
				.filter(PropertyCondition::getActive)
				.forEach(c -> c.updateActiveStatus(false));
			// 매물도 비활성화
			property.updateActiveStatus(false);
		}
		return CreateContractResDto.toDto(contract.getId());
	}

	/**
	 * 계약 수정
	 * @param id 계약 id
	 * @param dto 계약 수정 요청 dto
	 */
	@Transactional
	@Override
	public void updateContract(Long id, ContractUpdateReqDto dto) {
		Contract contract = contractReader.findBy(id);
		// 계약할 매물 조건 조회
		PropertyCondition propertyCondition = propertyReader.findPropertyConditionBy(contract.getPropertyCondition().getId());
		// 매물 조회
		Property property = propertyReader.findPropertyBy(propertyCondition.getProperty().getId());
		Customer customer = findCustomerById(Optional.ofNullable(dto.getCustomerId()).orElse(contract.getCustomer().getId()));
		// 매물을 등록한 고객과 계약할 고객이 동일한 경우 예외 처리
		isSameCustomerAndProperty(customer, property);

		// 계약 상태 변경
		// 거래 진행중 상태로 바꿀 경우
		if (dto.getContractStatus() == ContractStatus.IN_PROGRESS) {
		    // 같은 고객과 매물에 대한 완료되지 않은 계약이 있는지 확인
		    // 완료되지 않은 계약이 있으면 예외
			contractReader.existsInProgressContract(property, customer, dto.getContractStatus());
		}

		// 계약 정보 수정
		contract.updateContract(dto);
		contract.changeCustomer(customer);
	}

	/**
	 * 전체 계약 조회 (검색 포함)
	 * @param searchDto 계약 검색 조건 DTO
	 * @param pageable 페이지네이션 정보
	 * @param agentId 공인중개사 ID
	 * @return 계약 정보 응답 DTO LIST
	 */
	@Override
	@Transactional(readOnly = true)
	public ContractListResDto findContracts(ContractSearchDto searchDto, Pageable pageable, Long agentId) {
		// 페이지네이션 적용하여 계약 조회
		// 해당 공인중개사가 체결한 계약만 조회
		Page<Contract> contractPage = contractReader.searchContracts(agentId, searchDto, pageable);
		// 계약 엔티티를 dto 로 변환하여 리스트로 반환
		Page<FindContractResDto> response = contractPage.map(FindContractResDto::fromEntity);
		return ContractListResDto.fromPage(response);
	}

	/**
	 * 계약 상세 정보 조회
	 * @param id 계약 ID
	 * @return 계약 정보 응답 DTO
	 */
	@Override
	@Transactional(readOnly = true)
	public FindContractResDto findContract(Long id) {
		Contract contract = contractReader.findBy(id);
		return FindContractResDto.fromEntity(contract);
	}

	/**
	 * 계약 삭제
	 * @param id 삭제할 계약 id
	 */
	@Transactional
	@Override
	public void deleteContract(Long id) {
		Contract contract = contractReader.findBy(id);
		contract.softDelete();
	}

	/**
	 * 매물을 의뢰한 고객과 계약할 고객이 동일한 경우 예외 처리
	 * @param customer 계약할 고객
	 * @param property 계약할 매물
	 */
	public void isSameCustomerAndProperty(Customer customer, Property property) {
		if (property.getCustomer().getId().equals(customer.getId())) {
			throw new BusinessException(ErrorCode.CONTRACT_PROPERTY_CUSTOMER_SAME);
		}
	}

	/**
	 * 고객 id 로 존재 여부 확인
	 * @param id 고객 ID
	 * @return 고객 ID로 계약을 찾았을 경우, Customer 리턴
	 *         고객을 찾지 못했을 경우, exception 처리
	 */
	public Customer findCustomerById(Long id) {
		Customer customer = customerRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 고객입니다.", "CUSTOMER_NOT_FOUND"));
		return customer;
	}

	/**
	 * status 가 반드시 ACTIVE 인 중개사만 조회
	 * @param agentId 중개사 ID
	 * @return 중개사 엔티티
	 * @throws ResourceNotFoundException 해당 중개사를 찾을 수 없는 경우
	 */
	private Agent findAgentById(Long agentId) {
		return agentRepository.findByIdAndStatus(agentId, AgentStatus.ACTIVE)
			.orElseThrow(() -> new ResourceNotFoundException("해당 중개사를 찾을 수 없습니다.", "AGENT_NOT_FOUND"));
	}
}
