package com.househub.backend.domain.contract.service.impl;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
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
import org.springframework.data.domain.PageRequest;
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

    /**
     *
     * @param dto 해당 매물에 계약 등록하는 DTO
     * @return 등록한 계약 id 를 반환하는 DTO
     */
    @Override
    @Transactional // 계약 등록
    public CreateContractResDto createContract(ContractReqDto dto) {
        // 계약할 매물 조회
        Property property = findPropertyById(dto.getPropertyId());
        // 계약할 고객 조회
        Customer customer = findCustomerById(dto.getCustomerId());
        // 고객, 매물, 계약 상태가 동일한 계약이면 예외 처리 ( 기존에 존재하는 contract 인지 확인 )
        existsByContractAndProperty(customer, property);
        // dto → entity 변환 후 저장
        Contract contract = dto.toEntity(property, customer);
        contractRepository.save(contract);
        // 응답 객체 리턴
        return new CreateContractResDto(contract.getContractId());
    }

    /**
     *
     * @param contractId 계약 id
     * @param dto 계약 수정 요청 dto
     */
    @Transactional
    @Override // 계약 수정
    public void updateContract(Long contractId, ContractReqDto dto) {
        Contract contract = findContractById(contractId);
        // 고객 조회
        Customer customer = findCustomerById(dto.getCustomerId());
        // 매물 조회
        Property property = findPropertyById(dto.getPropertyId());
        // 판매 중으로 변경하는 경우
        if (dto.getContractStatus() != ContractStatus.SOLD_OUT) {
            // 고객, 매물이 동일한 계약이면 예외 처리 ( 기존에 존재하는 contract 인지 확인 )
            existsByContractAndProperty(customer, property);
        }
        // 계약 정보 수정
        contract.updateContract(dto);
    }

    /**
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 계약 정보 응답 DTO LIST
     */
    @Override
    @Transactional(readOnly = true) // 전체 계약 조회
    public List<FindContractResDto> findContracts(int page, int size) {
        // Pageable 객체 생성 (페이지 번호, 페이지 크기)
        Pageable pageable = PageRequest.of(page, size);

        // 페이지네이션 적용하여 계약 조회
        Page<Contract> propertyPage = contractRepository.findAll(pageable);

        // 매물 엔티티를 dto 로 변환하여 리스트로 반환
        return propertyPage.stream()
                .map(FindContractResDto::toDto)
                .toList();
    }

    /**
     *
     * @param id 삭제할 계약 id
     */
    @Transactional
    @Override // 계약 삭제
    public void deleteContract(Long id) {
        Contract contract = findContractById(id);
        contract.deleteContract();
    }

    // 해당 계약 id 존재 여부 확인
    /**
     *
     * @param id 계약 ID
     * @return 계약 ID로 계약을 찾았을 경우, Contract 리턴
     *         계약을 찾지 못했을 경우, exception 처리
     */
    public Contract findContractById(Long id) {
        Contract contract = contractRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 계약입니다.", "CONTRACT_NOT_FOUND"));
        return contract;
    }

    // 고객 id 로 존재 여부 확인
    /**
     *
     * @param id
     * @return 고객 ID로 계약을 찾았을 경우, Customer 리턴
     *         고객을 찾지 못했을 경우, exception 처리
     */
    public Customer findCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 고객입니다.", "CUSTOMER_NOT_FOUND"));
        return customer;
    }

    // 매물 id 로 존재 여부 확인
    /**
     *
     * @param id 매물 ID
     * @return 매물 ID로 계약을 찾았을 경우, Property 리턴
     *         매물을 찾지 못했을 경우, exception 처리
     */
    public Property findPropertyById(Long id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 매물입니다.", "PROPERTY_NOT_FOUND"));
        return property;
    }

    // 고객과 매물이 동일한 계약이 이미 존재하는지 확인
    // 계약 리스트 중 판매중인 계약이 있으면 예외 처리 되도록 구현
    /**
     *
     * @param customer 계약을 하는 고객
     * @param property 계약하는 매물
     */
    public void existsByContractAndProperty(Customer customer, Property property) {
        boolean isExist = contractRepository.existsByCustomerAndPropertyAndStatusNot(customer, property, ContractStatus.SOLD_OUT);
        if(isExist) {
            throw new AlreadyExistsException("해당 고객은 현재 이 매물에 대한 계약을 진행 중이거나 취소, 완료되지 않은 계약이 있습니다.",
                    "CONTRACT_ALREADY_EXISTS");
        }
    }
}
