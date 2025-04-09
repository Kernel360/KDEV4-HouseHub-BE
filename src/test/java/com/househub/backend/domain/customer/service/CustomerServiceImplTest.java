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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.customer.dto.CustomerListResDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.impl.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AgentRepository agentRepository;

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
        Long agentId = 1L;
        Agent mockAgent = Agent.builder().id(agentId).name("Test Agent").build();
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(mockAgent));
        when(customerRepository.findByEmail(createCustomerReqDto.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // when
        CreateCustomerResDto result = customerService.createCustomer(createCustomerReqDto, agentId);

        // then
        assertNotNull(result);
        assertEquals(expectedResponse.getEmail(), result.getEmail());
        assertEquals(expectedResponse.getName(), result.getName());
        assertEquals(expectedResponse.getAgeGroup(), result.getAgeGroup());
        assertEquals(expectedResponse.getContact(), result.getContact());
        assertEquals(expectedResponse.getGender(), result.getGender());
        assertEquals(expectedResponse.getMemo(), result.getMemo());
        assertEquals(expectedResponse.getDeletedAt(), result.getDeletedAt());

        verify(agentRepository).findById(agentId);
        verify(customerRepository).findByEmail(createCustomerReqDto.getEmail());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 고객 생성 시 예외 발생")
    void createCustomer_EmailAlreadyExists() {
        // given
        Long agentId = 1L; // Mock agentId
        Agent mockAgent = Agent.builder().id(agentId).name("Test Agent").build();
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(mockAgent));

        when(customerRepository.findByEmail(createCustomerReqDto.getEmail())).thenReturn(Optional.of(customer));

        // when & then
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> customerService.createCustomer(createCustomerReqDto, agentId));

        assertEquals("해당 이메일(test@example.com)로 생성되었던 계정이 이미 존재합니다.", exception.getMessage());
        assertEquals("EMAIL_ALREADY_EXIST", exception.getCode());

        verify(agentRepository).findById(agentId);
        verify(customerRepository).findByEmail(createCustomerReqDto.getEmail());
        verify(customerRepository, never()).save(any(Customer.class));
    }


    @Test
    @DisplayName("삭제되지 않은 모든 고객 조회 성공 - 페이지네이션 및 검색 조건 적용")
    void findAllByDeletedAtIsNull_Success_WithPaginationAndSearchDto() {
        // given
        Long agentId = 1L;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        // searchDto 초기화
        String keyword = "test@example.com";

        // Mock 데이터 생성
        Agent mockAgent = Agent.builder()
            .id(agentId)
            .name("Test Agent")
            .build();

        Customer mockCustomer = Customer.builder()
            .email("test@example.com")
            .name("Test User")
            .agent(mockAgent)
            .build();

        // Mock 페이징 데이터 생성
        Page<Customer> mockCustomerPage = new PageImpl<>(List.of(mockCustomer), pageable, 1);

        // Mock 리포지토리 동작 정의
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(mockAgent));
        when(customerRepository.findAllByAgentAndFiltersAndDeletedAtIsNull(
            eq(mockAgent.getId()),
            eq(keyword),
            eq(pageable)))
            .thenReturn(mockCustomerPage);

        // when
        CustomerListResDto result = customerService.findAllByDeletedAtIsNull(keyword, agentId, pageable);

        // then
        assertAll(
            () -> assertEquals(1, result.getPagination().getTotalElements()), // 전체 요소 개수
            () -> assertEquals(1, result.getPagination().getTotalPages()),    // 전체 페이지 수
            () -> assertEquals(1, result.getContent().size()),                // 현재 페이지 요소 개수
            () -> assertEquals("test@example.com", result.getContent().get(0).getEmail()),
            () -> assertEquals("Test User", result.getContent().get(0).getName())
        );

        // 리포지토리 호출 검증
        verify(agentRepository).findById(agentId);
        verify(customerRepository).findAllByAgentAndFiltersAndDeletedAtIsNull(
            eq(mockAgent.getId()),
            eq(keyword),
            eq(pageable));
    }


    @Test
    @DisplayName("ID로 삭제되지 않은 고객 조회 성공")
    void findByIdAndDeletedAtIsNull_Success() {
        // given
        Long id = 1L;
        Long agentId = 1L;
        Agent mockAgent = Agent.builder().id(agentId).name("Test Agent").build();

        when(agentRepository.findById(agentId)).thenReturn(Optional.of(mockAgent));
        when(customerRepository.findByIdAndAgentAndDeletedAtIsNull(id, mockAgent)).thenReturn(Optional.of(customer));

        // when
        CreateCustomerResDto result = customerService.findByIdAndDeletedAtIsNull(id, agentId);

        // then
        assertNotNull(result);
        assertEquals(expectedResponse.getEmail(), result.getEmail());
        verify(agentRepository).findById(agentId);
        verify(customerRepository).findByIdAndAgentAndDeletedAtIsNull(id, mockAgent);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 고객 조회 시 예외 발생")
    void findByIdAndDeletedAtIsNull_NotFound() {
        // given
        Long invalidId = 2L;
        Long agentId = 1L;
        Agent mockAgent = Agent.builder().id(agentId).name("Test Agent").build();

        when(agentRepository.findById(agentId)).thenReturn(Optional.of(mockAgent));
        when(customerRepository.findByIdAndAgentAndDeletedAtIsNull(invalidId, mockAgent)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> customerService.findByIdAndDeletedAtIsNull(invalidId, agentId));

        assertEquals("해당 아이디를 가진 고객이 존재하지 않습니다:2", exception.getMessage());
        assertEquals("CUSTOMER_NOT_FOUND", exception.getCode());
        verify(agentRepository).findById(agentId);
        verify(customerRepository).findByIdAndAgentAndDeletedAtIsNull(invalidId, mockAgent);
    }

    @Test
    @DisplayName("존재하지 않는 공인중개사로 고객 조회 시 예외 발생")
    void findByIdAndDeletedAtIsNull_AgentNotFound() {
        // given
        Long id = 1L;
        Long invalidAgentId = 999L;

        when(agentRepository.findById(invalidAgentId)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> customerService.findByIdAndDeletedAtIsNull(id, invalidAgentId));

        assertEquals("공인중개사가 존재하지 않습니다.", exception.getMessage());
        assertEquals("AGENT_NOT_FOUND", exception.getCode());

        verify(agentRepository).findById(invalidAgentId);
        verify(customerRepository, never()).findByIdAndAgentAndDeletedAtIsNull(any(), any());
    }

    @Test
    @DisplayName("고객 정보 수정 성공")
    void updateCustomer_Success() {
        // given
        Long id = 1L;
        Long agentId = 1L;
        CreateCustomerReqDto updateRequest = CreateCustomerReqDto.builder()
            .email("updated@example.com")
            .name("김철수")
            .build();

        Agent mockAgent = Agent.builder().id(agentId).name("Test Agent").build();
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(mockAgent));
        when(customerRepository.findByEmail(updateRequest.getEmail())).thenReturn(Optional.empty());
        when(customerRepository.findByIdAndAgentAndDeletedAtIsNull(id, mockAgent)).thenReturn(Optional.of(customer));

        // when
        CreateCustomerResDto result = customerService.updateCustomer(id, updateRequest, agentId);

        // then
        assertEquals(updateRequest.getEmail(), result.getEmail());
        assertEquals(updateRequest.getName(), result.getName());
        verify(agentRepository).findById(agentId);
        verify(customerRepository).findByEmail(updateRequest.getEmail());
        verify(customerRepository).findByIdAndAgentAndDeletedAtIsNull(id, mockAgent);
    }


    @Test
    @DisplayName("고객 수정 시 다른 사용자의 이메일 사용 시 AlreadyExistsException 발생")
    void updateCustomer_EmailConflict() {
        // given
        Long customerId = 1L;
        Long agentId = 1L;
        CreateCustomerReqDto updateRequest = CreateCustomerReqDto.builder()
            .email("conflict@example.com")
            .build();

        Agent mockAgent = Agent.builder().id(agentId).build();
        Customer existingCustomer = Customer.builder()
            .id(customerId)
            .email("original@example.com")
            .agent(mockAgent)
            .build();
        Customer otherCustomer = Customer.builder()
            .id(2L)
            .email("conflict@example.com")
            .build();

        // Agent 조회 Mocking
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(mockAgent));

        // 고객 조회 Mocking
        when(customerRepository.findByIdAndAgentAndDeletedAtIsNull(customerId, mockAgent))
            .thenReturn(Optional.of(existingCustomer));

        // 이메일 충돌 Mocking
        when(customerRepository.findByEmail("conflict@example.com"))
            .thenReturn(Optional.of(otherCustomer));

        // when & then
        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
            () -> customerService.updateCustomer(customerId, updateRequest, agentId));

        assertEquals("이미 사용 중인 이메일입니다: conflict@example.com", exception.getMessage());

        // verify
        verify(agentRepository).findById(agentId);
        verify(customerRepository).findByIdAndAgentAndDeletedAtIsNull(customerId, mockAgent);
        verify(customerRepository).findByEmail("conflict@example.com");
    }




    @Test
    @DisplayName("존재하지 않는 고객 수정 시도")
    void updateCustomer_NotFound() {
        // given
        Long invalidId = 999L;
        Long agentId = 1L;

        Agent mockAgent = Agent.builder().id(agentId).name("Test Agent").build();

        when(agentRepository.findById(agentId)).thenReturn(Optional.of(mockAgent));
        when(customerRepository.findByIdAndAgentAndDeletedAtIsNull(invalidId, mockAgent)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> customerService.updateCustomer(invalidId, createCustomerReqDto, agentId));

        assertEquals("해당 고객이 존재하지 않습니다:", exception.getMessage());

        verify(agentRepository).findById(agentId);
        verify(customerRepository).findByIdAndAgentAndDeletedAtIsNull(invalidId, mockAgent);
    }

    @Test
    @DisplayName("존재하지 않는 공인중개사로 고객 수정 시 예외 발생")
    void updateCustomer_AgentNotFound() {
        // given
        Long id = 1L;
        Long invalidAgentId = 999L;

        when(agentRepository.findById(invalidAgentId)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> customerService.updateCustomer(id, createCustomerReqDto, invalidAgentId));

        assertEquals("공인중개사가 존재하지 않습니다.", exception.getMessage());

        verify(agentRepository).findById(invalidAgentId);
        verify(customerRepository, never()).findByEmail(anyString());
    }

    @Test
    @DisplayName("고객 소프트 딜리트 성공")
    void deleteCustomer_Success() {
        // given
        Long id = 1L;
        Long agentId = 1L;

        Agent mockAgent = Agent.builder().id(agentId).name("Test Agent").build();
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(mockAgent));
        when(customerRepository.findByIdAndAgentAndDeletedAtIsNull(id, mockAgent)).thenReturn(Optional.of(customer));

        // when
        CreateCustomerResDto result = customerService.deleteCustomer(id, agentId);

        // then
        assertNotNull(result.getDeletedAt());
        assertTrue(result.getDeletedAt().isBefore(LocalDateTime.now().plusSeconds(1))); // 삭제 시간 검증
        verify(agentRepository).findById(agentId);
        verify(customerRepository).findByIdAndAgentAndDeletedAtIsNull(id, mockAgent);
    }

    @Test
    @DisplayName("존재하지 않는 고객 삭제 시도")
    void deleteCustomer_NotFound() {
        // given
        Long invalidId = 999L;
        Long agentId = 1L;

        Agent mockAgent = Agent.builder().id(agentId).name("Test Agent").build();
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(mockAgent));
        when(customerRepository.findByIdAndAgentAndDeletedAtIsNull(invalidId, mockAgent)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> customerService.deleteCustomer(invalidId, agentId));

        assertEquals("해당 고객이 존재하지 않습니다:", exception.getMessage());

        verify(agentRepository).findById(agentId);
        verify(customerRepository).findByIdAndAgentAndDeletedAtIsNull(invalidId, mockAgent);
    }

    @Test
    @DisplayName("존재하지 않는 공인중개사로 고객 삭제 시 예외 발생")
    void deleteCustomer_AgentNotFound() {
        // given
        Long id = 1L;
        Long invalidAgentId = 999L;

        when(agentRepository.findById(invalidAgentId)).thenReturn(Optional.empty());

        // when & then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> customerService.deleteCustomer(id, invalidAgentId));

        assertEquals("공인중개사가 존재하지 않습니다.", exception.getMessage());

        verify(agentRepository).findById(invalidAgentId);
        verify(customerRepository, never()).findByIdAndAgentAndDeletedAtIsNull(any(), any());
    }


}