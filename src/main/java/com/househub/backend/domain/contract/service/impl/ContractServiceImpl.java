package com.househub.backend.domain.contract.service.impl;

import com.househub.backend.domain.contract.validator.ContractValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.contract.dto.ContractListResDto;
import com.househub.backend.domain.contract.dto.CreateContractReqDto;
import com.househub.backend.domain.contract.dto.ContractSearchDto;
import com.househub.backend.domain.contract.dto.CreateContractResDto;
import com.househub.backend.domain.contract.dto.FindContractResDto;
import com.househub.backend.domain.contract.dto.UpdateContractReqDto;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.service.ContractReader;
import com.househub.backend.domain.contract.service.ContractService;
import com.househub.backend.domain.contract.service.ContractStore;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerReader;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.service.PropertyReader;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

	private final ContractReader contractReader;
	private final ContractStore contractStore;
	private final PropertyReader propertyReader;
	private final CustomerReader customerReader;
	private final ContractValidator contractValidator;

	/**
	 * 계약 등록
	 * @param dto 계약 등록 요청 DTO
	 * @param agentDto 공인중개사 DTO
	 * @return 등록한 계약 id 를 반환하는 DTO
	 */
	@Override
	@Transactional
	public CreateContractResDto createContract(CreateContractReqDto dto, AgentResDto agentDto) {
		// 계약할 매물 조회
		Property property = propertyReader.findByIdOrThrow(dto.getPropertyId(), agentDto.getId());
		Customer customer = null;
		if (dto.getCustomerId() != null) {
			// 계약자 존재할 경우 고객 조회
			customer = customerReader.findByIdOrThrow(dto.getCustomerId(), agentDto.getId());
			// 매물을 등록한 고객과 계약할 고객이 동일한 경우 예외 처리
			validateCustomerIsNotPropertyOwner(customer, property);
			// 같은 계약자가 동일한 매물에 대해 진행중인 계약이 있는지 확인
			contractValidator.validateNoInProgressContract(customer, property);
		}
		// dto → entity 변환 후 저장
		Agent agent = agentDto.toEntity();
		Contract contract = dto.toEntity(property, customer, agent);
		// 계약 가능 상태로 등록할 경우, 매물 활성화 상태 enable
		if(dto.getContractStatus() == ContractStatus.AVAILABLE) {
			property.enable();
		}
		contract = contractStore.create(contract);
		// 응답 객체 리턴
		return CreateContractResDto.fromEntity(contract);
	}

	/**
	 * 계약 수정
	 * @param id 계약 id
	 * @param dto 계약 수정 요청 dto
	 */
	@Transactional
	@Override
	public void updateContract(Long id, UpdateContractReqDto dto, AgentResDto agentDto) {
		Contract contract = contractReader.findByIdOrThrow(id, agentDto.getId());
		// 해당 계약의 고객이 삭제된 고객일 경우 수정 불가
		if(contract.getCustomer() != null && contract.getCustomer().getDeletedAt() != null) {
			throw new BusinessException(ErrorCode.CONTRACT_CUSTOMER_ALREADY_DELETED);
		}
		// 매물 조회
		Property property = contract.getProperty();
		// 계약자를 수정한 경우, 검증
		if(dto.getCustomerId() != null) {
			// 고객 조회
			Customer customer = customerReader.findByIdOrThrow(dto.getCustomerId(), agentDto.getId());
			// 매물을 등록한 고객과 계약할 고객이 동일한 경우 예외 처리
			validateCustomerIsNotPropertyOwner(customer, property);
			// 같은 계약자가 동일한 매물에 대해 진행중인 계약이 있는지 확인
			contractValidator.validateNoInProgressContract(customer, property);
			// 계약자 수정
			contractStore.updateCustomer(contract, customer);
		}
		// 계약 정보 수정
		contractStore.update(contract, dto);
	}

	/**
	 * 전체 계약 조회 (검색 포함)
	 * @param searchDto 계약 검색 조건 DTO
	 * @param pageable 페이지네이션 정보
	 * @param agentDto 공인중개사 DTO
	 * @return 계약 정보 응답 DTO LIST
	 */
	@Override
	@Transactional(readOnly = true)
	public ContractListResDto findContracts(ContractSearchDto searchDto, Pageable pageable, AgentResDto agentDto) {
		Agent agent = agentDto.toEntity();
		// 해당 공인중개사가 체결한 계약만 조회
		Page<Contract> contractPage = contractReader.findPageBySearchDto(searchDto, pageable, agent.getId());
		// 계약 엔티티를 dto 로 변환하여 리스트로 반환
		Page<FindContractResDto> response = contractPage.map(FindContractResDto::toDto);
		return ContractListResDto.fromPage(response);
	}

	@Transactional(readOnly = true)
	@Override
	public ContractListResDto findAllByCustomer(Long id, Pageable pageable, Long agentId) {
		// 페이지네이션 적용하여 계약 조회
		// 해당 공인중개사가 체결한 계약만 조회
		Page<Contract> contractPage = contractReader.findContractsByAgentAndCustomer(
			agentId,
			id,
			pageable
		);
		// 계약 엔티티를 dto 로 변환하여 리스트로 반환
		Page<FindContractResDto> response = contractPage.map(FindContractResDto::toDto);
		return ContractListResDto.fromPage(response);
	}

	@Transactional(readOnly = true)
	@Override
	public ContractListResDto findAllByProperties(List<Property> properties, Pageable pageable, Long agentId) {

		Page<Contract> contractPage = contractReader.findContractsByProperties(
			agentId,
			properties,
			pageable
		);

		Page<FindContractResDto> response = contractPage.map(FindContractResDto::toDto);
		return ContractListResDto.fromPage(response);
	}

	/**
	 * 계약 상세 정보 조회
	 * @param id 계약 ID
	 * @param agentDto 공인중개사 DTO
	 * @return 계약 정보 응답 DTO
	 */
	@Override
	@Transactional(readOnly = true)
	public FindContractResDto findContract(Long id, AgentResDto agentDto) {
		Contract contract = contractReader.findByIdOrThrow(id, agentDto.getId());
		return FindContractResDto.toDto(contract);
	}

	/**
	 * 계약 삭제
	 * @param id 삭제할 계약 ID
	 * @param agentDto 공인중개사 DTO
	 */
	@Transactional
	@Override
	public void deleteContract(Long id, AgentResDto agentDto) {
		Contract contract = contractReader.findByIdOrThrow(id, agentDto.getId());
		// 해당 매물에 대한 계약이 모두 삭제된 경우 매물 활성화 상태를 false 로 변경
		if(contract.getProperty().getContracts().stream().filter(
			c -> c.getDeletedAt() == null).count() == 0) {
			contract.getProperty().enable();
		}
		contractStore.delete(contract);
	}

	/**
	 * 매물을 의뢰한 고객과 계약할 고객이 동일한 경우 예외 처리
	 * @param customer 계약할 고객
	 * @param property 계약할 매물
	 */
	private void validateCustomerIsNotPropertyOwner(Customer customer, Property property) {
		if (property.getCustomer().getId().equals(customer.getId())) {
			throw new BusinessException(ErrorCode.CONTRACT_PROPERTY_CUSTOMER_SAME);
		}
	}
}
