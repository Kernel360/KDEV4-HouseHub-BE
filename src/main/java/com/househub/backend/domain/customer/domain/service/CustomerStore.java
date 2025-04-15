package com.househub.backend.domain.customer.domain.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.domain.entity.Customer;
import com.househub.backend.domain.customer.interfaces.dto.CreateCustomerReqDto;

public interface CustomerStore {
	Customer createCustomer(CreateCustomerReqDto initCustomer, Agent agent);

	List<Customer> createCustomersByExcel(MultipartFile file, Agent agent);

	Customer updateCustomer(Long id, CreateCustomerReqDto request ,Agent agent);

	Customer deleteCustomer(Long id, Agent agent);
}