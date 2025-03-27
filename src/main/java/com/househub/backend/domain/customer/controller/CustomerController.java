package com.househub.backend.domain.customer.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.customer.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    // 고객 등록
    // 이메일이 중복되는 경우, 가입이 되지 않게 해야함
    @PostMapping("")
    public ResponseEntity<SuccessResponse<CreateCustomerResDto>> createCustomer(
            @Valid @RequestBody CreateCustomerReqDto request) {

        CreateCustomerResDto response = customerService.createCustomer(request);
        return ResponseEntity.ok(SuccessResponse.success("고객 등록이 완료되었습니다.", "CUSTOMER_REGISTER_SUCCESS", response));
    }

    // 고객 목록 조회
    // 삭제 처리된 고객은 조회되지 않게 로직 생성
    @GetMapping("")
    public ResponseEntity<SuccessResponse<List<CreateCustomerResDto>>> findAllCustomer() {
        List<CreateCustomerResDto> response = customerService.findAllByDeletedAtIsNull();

        return ResponseEntity.ok(SuccessResponse.success("고객 목록 조회에 성공했습니다.", "FIND_ALL_CUSTOMER_SUCCESS", response));
    }

    // 고객 상세 정보 조회
    // 삭제 처리된 고객은 조회되지 않게 예외처리
    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse<CreateCustomerResDto>> findOneCustomer(@PathVariable Long id) {
        CreateCustomerResDto response = customerService.findByIdAndDeletedAtIsNull(id);

        return ResponseEntity.ok(SuccessResponse.success("고객 상세 조회가 완료되었습니다.", "FIND_CUSTOMER_SUCCESS", response));
    }

    // 고객 수정
    // 이메일이 중복되는 경우, 에러처리 해야함
    @PutMapping("/{id}")
    public ResponseEntity<SuccessResponse<CreateCustomerResDto>> updateCustomer(@PathVariable Long id, @Valid @RequestBody CreateCustomerReqDto request) {
        CreateCustomerResDto response = customerService.updateCustomer(id, request);

        return ResponseEntity.ok(SuccessResponse.success("고객 정보 수정이 완료되었습니다.", "UPDATE_CUSTOMER_SUCCESS", response));
    }

    // 고객 삭제
    // 이미 삭제된 고객은 못하게 막기
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse<CreateCustomerResDto>> deleteCustomer(@PathVariable Long id) {
        CreateCustomerResDto response = customerService.deleteCustomer(id);

        return ResponseEntity.ok(SuccessResponse.success("해당 고객의 삭제가 완료되었습니다.", "DELETE_CUSTOMER_SUCCESS", response));
    }

    // excel 업로드
    @PostMapping("/upload")
    public ResponseEntity<SuccessResponse<List<CreateCustomerResDto>>> createCustomersByExcel(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }
        List<CreateCustomerResDto> response = customerService.createCustomersByExcel(file);
        return ResponseEntity.ok(SuccessResponse.success("고객 정보 등록 완료", "CUSTOMER_REGISTER_SUCCESS", response));
    }

    // excel 템플릿 다운로드
    @GetMapping("/download")
    public ResponseEntity<Resource> downloadExcelForm(){
        // 템플릿 파일 로드
        Resource resource = new ClassPathResource("templates/excel_template.xlsx");

        // 파일 이름 설정
        String filename = "excel_template.xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
}
