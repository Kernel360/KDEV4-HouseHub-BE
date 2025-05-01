package com.househub.backend.domain.customer.service.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.domain.crawlingProperty.entity.Tag;
import com.househub.backend.domain.crawlingProperty.repository.TagRepository;
import com.househub.backend.domain.customer.dto.CustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.entity.CustomerTagMap;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.repository.CustomerTagMapRepository;
import com.househub.backend.domain.customer.service.CustomerStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Transactional
public class CustomerStoreImpl implements CustomerStore {

	private final CustomerRepository customerRepository;

	@Override
	public Customer create(Customer customer) {
		return customerRepository.save(customer);
	}

	@Override
	public List<Customer> createAll(List<Customer> customers) {
		return customerRepository.saveAll(customers);
	}

	@Override
	public Customer update(Customer customer, CustomerReqDto request, List<Tag> tags) {
		List<Tag> filteredTags = tags == null
			? Collections.emptyList()
			: tags.stream().filter(Objects::nonNull).collect(Collectors.toList());
		customer.update(request, filteredTags);
		return customerRepository.save(customer);
	}

	@Override
	public Customer delete(Customer customer) {
		// 소프트 딜리트
		customer.softDelete();
		return customer;
	}

	@Override
	public Customer restore(Customer customer) {
		customer.restore();
		return customer;
	}
}

