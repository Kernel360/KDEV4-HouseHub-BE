package com.househub.backend.domain.customer.service.impl;

import com.househub.backend.common.exception.EmailAlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public Customer createCustomer(CreateCustomerReqDto request) {
        // 이메일로 고객 조회
        Optional<Customer> customerOpt = customerRepository.findByEmail(request.getEmail());

        if (customerOpt.isPresent())
            throw new EmailAlreadyExistsException("해당 이메일로 생성되었던 계정이 이미 존재합니다.");

        // 새로운 고객 생성 및 저장
        return customerRepository.save(request.toEntity());
    }


    public List<Customer> findAllCustomer() {
        return customerRepository.findAll();
    }

    public List<Customer> findAllByDeletedAtIsNull() { return customerRepository.findAllByDeletedAtIsNull();}

    public Customer findOne(Long id) {
        return customerRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 ID의 고객을 찾을 수 없습니다."));
    }

    public Customer findByIdAndDeletedAtIsNull(Long id) {
        return customerRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new IllegalArgumentException("삭제되었거나 존재하지 않는 고객입니다."));
    }

    public Customer updateCustomer(Long id, CreateCustomerReqDto dto) {
        Customer customer = customerRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new IllegalArgumentException("삭제되었거나 존재하지 않는 고객입니다."));
        customer.update(dto);
        return customerRepository.save(customer);
    }


    public Customer deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 고객이 존재하지 않습니다: " + id));

        // 소프트 딜리트
        customer.delete();
        customerRepository.save(customer);

        return customer;
    }
};
