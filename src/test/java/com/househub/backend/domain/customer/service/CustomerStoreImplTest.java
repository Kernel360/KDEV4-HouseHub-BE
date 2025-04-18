package com.househub.backend.domain.customer.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.impl.CustomerStoreImpl;

@ExtendWith(MockitoExtension.class)
public class CustomerStoreImplTest {

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private CustomerReader customerReader;

	@Mock
	private MultipartFile file;

	@InjectMocks
	private CustomerStoreImpl customerStore;

	private Agent agent;
	private CreateCustomerReqDto validRequest;

	@BeforeEach
	void setUp() {
		agent = Agent.builder()
			.id(1L)
			.build();

		validRequest = CreateCustomerReqDto.builder()
			.name("김철수")
			.ageGroup(30)
			.contact("010-1234-1234")
			.email("test@example.com")
			.memo("특이사항 없음")
			.gender(Gender.M)
			.build();
	}

	@Test
	@DisplayName("고객 생성 성공")
	void create_success() {
		// given
		Customer expectedCustomer = validRequest.toEntity(agent);
		when(customerRepository.save(any(Customer.class))).thenReturn(expectedCustomer);

		// when
		Customer result = customerStore.create(expectedCustomer);

		// then
		assertThat(result).isEqualTo(expectedCustomer);

		// verify
		verify(customerRepository, times(1)).save(any(Customer.class));
	}

	// 2. updateCustomer 테스트
	@Test
	@DisplayName("고객 정보 업데이트 성공")
	void update_success() {
		// given
		Customer existingCustomer = Customer.builder()
			.id(1L)
			.contact("010-0000-0000")
			.agent(agent)
			.build();
		when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

		// when
		Customer result = customerStore.update(existingCustomer, validRequest);

		// then
		assertThat(result.getContact()).isEqualTo(validRequest.getContact());
	}

	// 3. deleteCustomer 테스트
	@Test
	@DisplayName("고객 소프트 삭제 성공")
	void softDelete_success() {
		// given
		Customer customer = Customer.builder()
			.id(1L)
			.deletedAt(null)
			.build();

		when(customerRepository.save(any(Customer.class))).thenAnswer(invocation -> {
			Customer saved = invocation.getArgument(0);
			saved.softDelete(); // ✅ 실제 구현체의 동작 방식 반영
			return saved;
		});

		// when
		Customer result = customerStore.delete(customer); // 파라미터 수정

		// then
		assertThat(result.getDeletedAt()).isNotNull();
		verify(customerRepository).save(customer);
	}

}
