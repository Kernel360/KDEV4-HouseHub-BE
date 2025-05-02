package com.househub.backend.domain.customer.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.entity.CustomerTagMap;
import com.househub.backend.domain.customer.service.CustomerExecutor;
import com.househub.backend.domain.customer.service.CustomerReader;
import com.househub.backend.domain.customer.service.CustomerStore;
import com.househub.backend.domain.customer.service.CustomerTagMapStore;
import com.househub.backend.domain.tag.entity.Tag;
import com.househub.backend.domain.tag.service.TagReader;
import com.househub.backend.domain.tag.service.TagStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomerExecutorImpl implements CustomerExecutor {
	private final CustomerReader customerReader;
	private final CustomerStore customerStore;
	private final CustomerTagMapStore customerTagMapStore;
	private final TagReader tagReader;
	private final TagStore tagStore;

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
	public Customer addTagsToCustomer(Customer customer, List<Long> tagIds) {
		List<Tag> tagList = tagStore.findAllById(tagIds);
		tagList.forEach(tag ->
			customer.getCustomerTagMaps().add(
				CustomerTagMap.builder()
					.customer(customer)
					.tag(tag)
					.build()
			));
		return customer;
	}

	@Override
	public Customer validateAndUpdate(Long id, CustomerReqDto request, Agent agent) {
		Customer customer = customerReader.findByIdOrThrow(id, agent.getId());
		if (!customer.getContact().equals(request.getContact())) {
			customerReader.checkDuplicatedByContact(request.getContact(), agent.getId());
		}

		// 태그 벌크 삭제 → 즉시 반영
		customerTagMapStore.deleteByCustomerId(customer.getId());

		List<Tag> tags = tagReader.findAllById(request.getTagIds());
		return customerStore.update(customer, request, tags);
	}

	@Override
	public Customer validateAndDelete(Long id, Agent agent) {
		Customer customer = customerReader.findByIdOrThrow(id, agent.getId());
		return customerStore.delete(customer);
	}
}
