package com.househub.backend.domain.customer.controller;

import com.househub.backend.domain.customer.dto.CustomerReqDto;
import com.househub.backend.domain.customer.dto.CustomerResDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    // 고객 등록
    @PostMapping("")
    public ResponseEntity createCustomer(@RequestBody CustomerReqDto request) {
        Long id = customerService.createCustomer(request);

        if(id != null){
            return ResponseEntity.ok("정상적으로 처리되었습니다.");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 고객 목록 조회
    @GetMapping("")
    public ResponseEntity<List<CustomerResDto>> findAllCustomer(){
        List<Customer> customers = customerService.findAllCustomer();


        if(customers != null){
            List<CustomerResDto> list = customers.stream().map(CustomerResDto::toDto).toList();
            return ResponseEntity.ok().body(list);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 고객 상세 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<CustomerResDto> findOneCustomer(@PathVariable Long id){
        Customer customer = customerService.findOne(id);

        if(customer != null){
            CustomerResDto dto = CustomerResDto.toDto(customer);
            return ResponseEntity.ok().body(dto);
        }  else {
            return ResponseEntity.notFound().build();
        }
    }
//
//    // 고객 수정
//    @PutMapping("/{id}")
//    public String updateCustomer(){
//
//    }
//
//    // 고객 삭제
//    @DeleteMapping("/{id}")
//    public String deleteCustomer(){
//
//    }

}
