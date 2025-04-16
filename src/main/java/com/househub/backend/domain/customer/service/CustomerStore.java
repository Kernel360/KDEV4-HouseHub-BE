package com.househub.backend.domain.customer.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;

public interface CustomerStore {
	Customer createCustomer(Customer customer);

	List<Customer> createCustomersByExcel(MultipartFile file, Agent agent);

	Customer updateCustomer(Long id, CreateCustomerReqDto request ,Agent agent);

	Customer deleteCustomer(Long id, Agent agent);
}