package com.househub.backend.domain.customer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.impl.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CreateCustomerReqDto createCustomerReqDto;
    private Customer customer;
    private CreateCustomerResDto expectedResponse;
    private Agent agent;


    @BeforeEach
    public void setUp() {
        createCustomerReqDto = CreateCustomerReqDto.builder()
                .email("test@example.com")
                .name("홍길동")
                .ageGroup(10)
                .contact("010-1234-5678")
                .gender(Gender.M)
                .memo("메모")
                .build();

        customer = Customer.builder()
                .id(1L)
                .email("test@example.com")
                .name("홍길동")
                .ageGroup(10)
                .contact("010-1234-5678")
                .gender(Gender.M)
                .memo("메모")
                .deletedAt(null)
                .build();

        expectedResponse = CreateCustomerResDto.builder()
                .email("test@example.com")
                .name("홍길동")
                .ageGroup(10)
                .contact("010-1234-5678")
                .gender(Gender.M)
                .memo("메모")
                .deletedAt(null)
                .build();

        agent = Agent.builder()
            .id(1L)
            .email("test@example.com")
            .name("이순신")
            .build();
    }


    @Test
    @DisplayName("새로운 고객 생성 성공")
    void createCustomer_Success() {
        // given
        when(customerRepository.findByEmail(createCustomerReqDto.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // when
        CreateCustomerResDto result = customerService.createCustomer(createCustomerReqDto, agent.getId());

        // then
        assertNotNull(result);
        assertEquals(expectedResponse.getEmail(), result.getEmail());
        assertEquals(expectedResponse.getName(), result.getName());
        assertEquals(expectedResponse.getAgeGroup(), result.getAgeGroup());
        assertEquals(expectedResponse.getContact(), result.getContact());
        assertEquals(expectedResponse.getGender(), result.getGender());
        assertEquals(expectedResponse.getMemo(), result.getMemo());
        assertEquals(expectedResponse.getDeletedAt(), result.getDeletedAt());

        verify(customerRepository).findByEmail(createCustomerReqDto.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 고객 생성 시 예외 발생")
    void createCustomer_EmailAlreadyExists() {
        // given
        when(customerRepository.findByEmail(createCustomerReqDto.getEmail())).thenReturn(Optional.of(customer));

        // when & then
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> customerService.createCustomer(createCustomerReqDto,agent.getId()));

        assertEquals("해당 이메일(test@example.com)로 생성되었던 계정이 이미 존재합니다.", exception.getMessage());
        assertEquals("EMAIL_ALREADY_EXIST", exception.getCode());

        verify(customerRepository).findByEmail(createCustomerReqDto.getEmail());
        verify(customerRepository, never()).save(any(Customer.class));
    }


    @Test
    @DisplayName("삭제되지 않은 모든 고객 조회 성공")
    void findAllByDeletedAtIsNull_Success() {
        // given
        List<Customer> customers = List.of(customer);
        when(customerRepository.findAllByDeletedAtIsNull()).thenReturn(customers);

        // when
        List<CreateCustomerResDto> result = customerService.findAllByDeletedAtIsNull();

        // then
        assertEquals(1, result.size());
        CreateCustomerResDto response = result.get(0);
        assertEquals(expectedResponse.getEmail(), response.getEmail());
        assertEquals(expectedResponse.getName(), response.getName());
        verify(customerRepository).findAllByDeletedAtIsNull();
    }

    @Test
    @DisplayName("ID로 삭제되지 않은 고객 조회 성공")
    void findByIdAndDeletedAtIsNull_Success() {
        // given
        when(customerRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(customer));

        // when
        CreateCustomerResDto result = customerService.findByIdAndDeletedAtIsNull(1L);

        // then
        assertNotNull(result);
        assertEquals(expectedResponse.getEmail(), result.getEmail());
        verify(customerRepository).findByIdAndDeletedAtIsNull(1L);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 고객 조회 시 예외 발생")
    void findByIdAndDeletedAtIsNull_NotFound() {
        // given
        Long invalidId = 2L;
        when(customerRepository.findByIdAndDeletedAtIsNull(invalidId)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerService.findByIdAndDeletedAtIsNull(invalidId));

        assertEquals("해당 아이디를 가진 고객이 존재하지 않습니다:2", exception.getMessage());
        assertEquals("CUSTOMER_NOT_FOUND", exception.getCode());
        verify(customerRepository).findByIdAndDeletedAtIsNull(invalidId);
    }

    @Test
    @DisplayName("고객 정보 수정 성공")
    void updateCustomer_Success() {
        // given
        Long id = 1L;
        CreateCustomerReqDto updateRequest = CreateCustomerReqDto.builder()
                .email("updated@example.com")
                .name("김철수")
                .build();

        when(customerRepository.findByEmail(updateRequest.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findByIdAndDeletedAtIsNull(id)).thenReturn(Optional.of(customer));

        // when
        CreateCustomerResDto result = customerService.updateCustomer(id, updateRequest);

        // then
        assertEquals(updateRequest.getEmail(), result.getEmail());
        assertEquals(updateRequest.getName(), result.getName());
        verify(customerRepository).findByEmail(updateRequest.getEmail());
        verify(customerRepository).findByIdAndDeletedAtIsNull(id);
    }

    @Test
    @DisplayName("수정 시 이미 존재하는 이메일 사용")
    void updateCustomer_EmailConflict() {
        // given
        Long id = 1L;
        CreateCustomerReqDto updateRequest = CreateCustomerReqDto.builder()
                .email("existing@example.com")
                .build();

        Customer otherCustomer = Customer.builder().id(2L).email("existing@example.com").build();
        when(customerRepository.findByEmail(updateRequest.getEmail())).thenReturn(Optional.of(otherCustomer));

        // when & then
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> customerService.updateCustomer(id, updateRequest));

        assertEquals("해당 이메일(existing@example.com)로 생성되었던 계정이 이미 존재합니다.", exception.getMessage());
        verify(customerRepository).findByEmail(updateRequest.getEmail());
        verify(customerRepository, never()).findByIdAndDeletedAtIsNull(any());
    }

    @Test
    @DisplayName("존재하지 않는 고객 수정 시도")
    void updateCustomer_NotFound() {
        // given
        Long invalidId = 999L;
        when(customerRepository.findByIdAndDeletedAtIsNull(invalidId)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerService.updateCustomer(invalidId, createCustomerReqDto));

        assertEquals("해당 고객이 존재하지 않습니다:", exception.getMessage());
        verify(customerRepository).findByIdAndDeletedAtIsNull(invalidId);
    }

    @Test
    @DisplayName("고객 소프트 딜리트 성공")
    void deleteCustomer_Success() {
        // given
        when(customerRepository.findByIdAndDeletedAtIsNull(1L)).thenReturn(Optional.of(customer));

        // when
        CreateCustomerResDto result = customerService.deleteCustomer(1L);

        // then
        assertNotNull(result.getDeletedAt());
        assertTrue(result.getDeletedAt().isBefore(LocalDateTime.now().plusSeconds(1))); // 삭제 시간 검증
        verify(customerRepository).findByIdAndDeletedAtIsNull(1L);
    }

    @Test
    @DisplayName("존재하지 않는 고객 삭제 시도")
    void deleteCustomer_NotFound() {
        // given
        Long invalidId = 999L;
        when(customerRepository.findByIdAndDeletedAtIsNull(invalidId)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerService.deleteCustomer(invalidId));

        assertEquals("해당 고객이 존재하지 않습니다:", exception.getMessage());
        verify(customerRepository).findByIdAndDeletedAtIsNull(invalidId);
    }
}