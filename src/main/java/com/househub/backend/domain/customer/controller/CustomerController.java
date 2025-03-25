package com.househub.backend.domain.customer.controller;

import com.househub.backend.common.exception.EmailAlreadyExistsException;
import com.househub.backend.common.response.ErrorResponse;
import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    // 고객 등록
    // 이메일이 중복되는 경우, 가입이 되지 않게 해야함
    @PostMapping("")
    public ResponseEntity<?> createCustomer(@Valid @RequestBody CreateCustomerReqDto request) {
        Customer customer = customerService.createCustomer(request);
        CreateCustomerResDto response = customer.toDto();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 고객 목록 조회
    // 삭제 처리된 고객은 조회되지 않게 로직 생성
    @GetMapping("")
    public ResponseEntity<List<CreateCustomerResDto>> findAllCustomer() {
        List<Customer> customers = customerService.findAllByDeletedAtIsNull();

        if (customers != null) {
            List<CreateCustomerResDto> list = customers.stream().map(Customer::toDto).toList();
            return ResponseEntity.ok().body(list);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 고객 상세 정보 조회
    // 삭제 처리된 고객은 조회되지 않게 로직 생성
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<CreateCustomerResDto>> findOneCustomer(@PathVariable Long id) {
        Customer customer = customerService.findByIdAndDeletedAtIsNull(id);

        return ResponseEntity.ok(SuccessResponse.success("회원 상세 조회가 완료되었습니다.","FIND_SUCCESS",null));
    }

    // 고객 수정
    // 이메일이 중복되는 경우, 에러처리 해야함
    @PutMapping("/{id}")
    public ResponseEntity<CreateCustomerResDto> updateCustomer(@PathVariable Long id, @RequestBody CreateCustomerReqDto reqDto) {
        Customer customer = customerService.updateCustomer(id, reqDto);

        if (customer != null) {
            CreateCustomerResDto resDto = customer.toDto();
            return ResponseEntity.ok().body(resDto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    // 고객 삭제
    // 이미 삭제된 고객은 못하게 막기
    @DeleteMapping("/{id}")
    public ResponseEntity<CreateCustomerResDto> deleteCustomer(@PathVariable Long id) {
        Customer customer = customerService.deleteCustomer(id);

        if (customer != null) {
            CreateCustomerResDto resDto = customer.toDto();
            return ResponseEntity.ok().body(resDto);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }


    // excel 업로드


    // excel 템플릿 다운로드


}
