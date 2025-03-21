package com.househub.backend.domain.customer.service;

import com.househub.backend.domain.customer.dto.CustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Long addCustomer(CustomerReqDto dto) {
        Customer customer = dto.toEntity();
        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer.getId();
    }

    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }
};
