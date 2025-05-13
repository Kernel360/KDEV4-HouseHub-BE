package com.househub.backend.domain.customer.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.househub.backend.domain.contract.enums.ContractStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.CustomerReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerReaderImpl implements CustomerReader {

	private final CustomerRepository customerRepository;

	@Override
	public Customer findByIdAndDeletedAtIsNotNullOrThrow(Long id, Long agentId) {
		return customerRepository.findByIdAndAgentIdAndDeletedAtIsNull(id, agentId).orElseThrow(() -> new ResourceNotFoundException("고객이 삭제되었거나 존재하지 않습니다.","CUSTOMER_NOT_FOUND"));
	}

	@Override
	public Customer findByIdOrThrow(Long id, Long agentId) {
		return customerRepository.findByIdAndAgentId(id, agentId).orElseThrow(() -> new ResourceNotFoundException("고객이 삭제되었거나 존재하지 않습니다.","CUSTOMER_NOT_FOUND"));
	}

	@Override
	public Customer findByContactOrThrow(String contact, Long agentId) {
		// 연락처로 고객 조회
		return customerRepository.findByContactAndAgentIdAndDeletedAtIsNull(contact,agentId).orElseThrow(() -> new ResourceNotFoundException("해당 전화번호로(" + contact + ")로 생성되었던 계정이 존재하지 않습니다.","CUSTOMER_NOT_FOUND"));
	}

	@Override
	public void checkDuplicatedByContact(String contact, Long agentId) {
		customerRepository.findByContactAndAgentIdAndDeletedAtIsNull(contact, agentId).ifPresent(customer -> {
			throw new AlreadyExistsException("해당 전화번호로(" + contact + ")로 생성되었던 고객이 이미 존재합니다.", "CUSTOMER_ALREADY_EXIST");
		});
	}

	@Override
	public void checkDuplicatedByEmail(String email, Long agentId) {
		if(email == null)
			return;
		customerRepository.findByEmailAndAgentIdAndDeletedAtIsNull(email, agentId).ifPresent(customer -> {
			throw new AlreadyExistsException("해당 이메일로(" + email + ")로 생성되었던 고객이 존재합니다.","CUSTOMER_ALREADY_EXIST");
		});
	}

	@Override
	public Page<Customer> findAllByKeyword(String keyword, Long agentId, Pageable pageable, boolean includeDeleted) {
		return customerRepository.findAllByAgentIdAndFiltersAndDeletedOnly(
			agentId,
			keyword,
			keyword,
			keyword,
			includeDeleted,
			pageable
		);
	}

	@Override
	public Page<Customer> findNewCustomers(Long agentId, Pageable pageable) {
		LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
		return customerRepository.findNewCustomersInLast7DaysByAgentId(agentId, sevenDaysAgo, pageable);
	}

	// 고객을 연락처와 agentId 로 조회
	@Override
	public Optional<Customer> findByContactAndAgentId(String contact, Long agentId) {
		return customerRepository.findByContactAndAgentId(contact, agentId);
	}

	@Override
	public List<Customer> findAllByBirthDate() {
		return customerRepository.findByBirthDate();
	}
}
