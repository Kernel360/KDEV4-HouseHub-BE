package com.househub.backend.domain.customer.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.consultation.service.ConsultationService;
import com.househub.backend.domain.crawlingProperty.entity.Tag;
import com.househub.backend.domain.crawlingProperty.repository.TagRepository;
import com.househub.backend.domain.customer.dto.CustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerExecutor;
import com.househub.backend.domain.customer.service.CustomerReader;
import com.househub.backend.domain.customer.service.CustomerStore;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerExecutorImpl implements CustomerExecutor {
	private final CustomerReader customerReader;
	private final CustomerStore customerStore;
	private final TagRepository tagRepository;

	@Transactional
	@Override
	public Customer findOrCreateCustomer(CustomerReqDto request, Agent agent) {
		// 전화번호로 고객 조회해서 존재하지 않으면 고객 생성
		return customerReader.findByContactAndAgentId(request.getContact(), agent.getId())
			.orElseGet(() -> customerStore.create(
				request.toEntity(agent)
			));
	}

	@Override
	public Customer validateAndRestore(Long id, Agent agent) {
		Customer customer = customerReader.findById(id, agent.getId());
		return customerStore.restore(customer);
	}

	@Override
	public Customer validateAndUpdate(Long id, CustomerReqDto request, Agent agent) {
		Customer customer = customerReader.findByIdOrThrow(id, agent.getId());
		if (!customer.getContact().equals(request.getContact())) {
			customerReader.checkDuplicatedByContact(request.getContact(), agent.getId());
		}
		List<Tag> tags = tagRepository.findAllById(request.getTagIds());
		return customerStore.update(customer, request, tags);
	}

	@Override
	public Customer validateAndDelete(Long id, Agent agent) {
		Customer customer = customerReader.findByIdOrThrow(id, agent.getId());
		return customerStore.delete(customer);
	}
}
