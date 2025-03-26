package com.househub.backend.domain.customer.service.impl;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public CreateCustomerResDto createCustomer(CreateCustomerReqDto request) {
        // 이메일로 고객 조회
        customerRepository.findByEmail(request.getEmail()).ifPresent(customer -> {
            throw new AlreadyExistsException("해당 이메일로 생성되었던 계정이 이미 존재합니다.", "EMAIL_ALREADY_EXIST");
        });

        // 새로운 고객 생성 및 저장
        return customerRepository.save(request.toEntity()).toDto();
    }

    public List<CreateCustomerResDto> findAllByDeletedAtIsNull() {
        return customerRepository.findAllByDeletedAtIsNull().stream().map(Customer::toDto).toList();
    }

    public CreateCustomerResDto findByIdAndDeletedAtIsNull(Long id) {
        Customer customer = customerRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 고객이 존재하지 않습니다:" + id, "CUSTOMER_NOT_FOUND"));
        return customer.toDto();
    }

    @Transactional
    public CreateCustomerResDto updateCustomer(Long id, CreateCustomerReqDto request) {
        customerRepository.findByEmail(request.getEmail()).ifPresent(customer -> {
            throw new AlreadyExistsException("해당 이메일로 생성되었던 계정이 이미 존재합니다.", "EMAIL_ALREADY_EXIST");
        });
        Customer customer = customerRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 고객이 존재하지 않습니다: " + id, "CUSTOMER_NOT_FOUND"));

        customer.update(request);

        return customer.toDto();
    }


    @Transactional
    public CreateCustomerResDto deleteCustomer(Long id) {
        Customer customer = customerRepository.findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new ResourceNotFoundException("해당 아이디를 가진 고객이 존재하지 않습니다: " + id, "CUSTOMER_NOT_FOUND"));

        // 소프트 딜리트
        customer.delete();

        return customer.toDto();
    }
}
