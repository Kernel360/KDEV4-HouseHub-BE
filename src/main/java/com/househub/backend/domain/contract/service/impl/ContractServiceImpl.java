package com.househub.backend.domain.contract.service.impl;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.contract.dto.*;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.enums.ContractStatus;
import com.househub.backend.domain.contract.repository.ContractRepository;
import com.househub.backend.domain.contract.service.ContractService;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final PropertyRepository propertyRepository;
    private final CustomerRepository customerRepository;
    private final AgentRepository agentRepository;

    /**
     * 계약 등록
     * @param dto 해당 매물에 계약 등록하는 DTO
     * @return 등록한 계약 id 를 반환하는 DTO
     */
    @Override
    @Transactional
    public CreateContractResDto createContract(ContractReqDto dto, Long agentId) {
        // 계약할 매물 조회
        Property property = findPropertyById(dto.getPropertyId());
        // 계약할 고객 조회
        Customer customer = findCustomerById(dto.getCustomerId());
        // 같은 고객과 매물에 대한 완료되지 않은 계약이 있는지 확인
        // 완료되지 않은 계약이 있으면 예외
        existsByContractAndProperty(customer, property);
        // dto → entity 변환 후 저장
        Agent agent = findAgentById(agentId);
        Contract contract = dto.toEntity(property, customer, agent);
        contract = contractRepository.save(contract);
        // 응답 객체 리턴
        return CreateContractResDto.toDto(contract.getId());
    }

    /**
     * 계약 수정
     * @param id 계약 id
     * @param dto 계약 수정 요청 dto
     */
    @Transactional
    @Override
    public void updateContract(Long id, ContractReqDto dto) {
        Contract contract = findContractById(id);
        // 고객 조회
        Customer customer = findCustomerById(dto.getCustomerId());
        // 매물 조회
        Property property = findPropertyById(dto.getPropertyId());
        // 거래 완료 상태 이외의 상태로 변경하는 경우
        if (dto.getContractStatus() != ContractStatus.COMPLETED) {
            // 같은 고객과 매물에 대한 완료되지 않은 계약이 있는지 확인
            // 완료되지 않은 계약이 있으면 예외
            existsByContractAndProperty(customer, property);
        }
        // 계약 정보 수정
        contract.updateContract(dto);
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
    public List<FindContractResDto> findContracts(ContractSearchDto searchDto, Pageable pageable, Long agentId) {
        // 페이지네이션 적용하여 계약 조회
        Agent agent = findAgentById(agentId);
        Page<Contract> contractPage = contractRepository.findContractsByRealEstateAndFilters(
            agent.getRealEstate().getId(),
            searchDto.getAgentName(),
            searchDto.getCustomerName(),
            searchDto.getContractType(),
            searchDto.getStatus(),
            pageable
        );
        // 계약 엔티티를 dto 로 변환하여 리스트로 반환
        List<FindContractResDto> response = contractPage.stream()
                .map(FindContractResDto::toDto)
                .toList();
        return response;
    }

    /**
     * 계약 삭제
     * @param id 삭제할 계약 id
     */
    @Transactional
    @Override
    public void deleteContract(Long id) {
        Contract contract = findContractById(id);
        contract.deleteContract();
    }

    /**
     * 해당 계약 id 존재 여부 확인
     * @param id 계약 ID
     * @return 계약 ID로 계약을 찾았을 경우, Contract 리턴
     *         계약을 찾지 못했을 경우, exception 처리
     */
    public Contract findContractById(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 계약입니다.", "CONTRACT_NOT_FOUND"));
        return contract;
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
     * 매물 id 로 존재 여부 확인
     * @param id 매물 ID
     * @return 매물 ID로 계약을 찾았을 경우, Property 리턴
     *         매물을 찾지 못했을 경우, exception 처리
     */
    public Property findPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 매물입니다.", "PROPERTY_NOT_FOUND"));
        return property;
    }

    /**
     * 해당 고객이 동일한 매물 계약하지 못하도록 예외 처리
     * (고객이 동일한 매물을 계약하는 경우 예외)
     * 동일 매물을 계약할 경우, 해당 매물의 계약 리스트의 모든 계약 상태가 '완료' 상태여야 하도록 처리
     * (해당 매물의 계약 리스트 중 판매중인 계약이 있으면 예외 처리 되도록 구현)
     * @param customer 계약을 하는 고객
     * @param property 계약하는 매물
     */
    public void existsByContractAndProperty(Customer customer, Property property) {
        boolean isExist = contractRepository.existsByCustomerAndPropertyAndStatusNot(customer, property, ContractStatus.COMPLETED);
        if(isExist) {
            throw new AlreadyExistsException("해당 고객은 본 매물에 대해 진행중인 계약이 존재합니다.",
                    "CONTRACT_ALREADY_EXISTS");
        }
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
