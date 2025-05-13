package com.househub.backend.domain.customer.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.impl.CustomerReaderImpl;

@ExtendWith(MockitoExtension.class)
class CustomerReaderImplTest {

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private CustomerReaderImpl customerReader;

	@Nested
	@DisplayName("getCustomerById")
	class GetCustomerByIdTest {
		@Test
		@DisplayName("고객이 존재할 때 정상 반환")
		void getCustomerById_success() {
			// given
			Long id = 1L;
			Long agentId = 2L;
			Customer customer = Customer.builder()
				.id(id)
				.agent(Agent.builder().id(agentId).build())
				.build();
			when(customerRepository.findByIdAndAgentIdAndDeletedAtIsNull(id, agentId)).thenReturn(
				Optional.of(customer));

			// when
			Customer result = customerReader.findByIdAndDeletedAtIsNotNullOrThrow(id, agentId);

			// then
			assertThat(result).isEqualTo(customer);
		}

		@Test
		@DisplayName("고객이 없을 때 예외 발생")
		void getCustomerById_notFound() {
			// given
			Long id = 1L;
			Long agentId = 2L;
			when(customerRepository.findByIdAndAgentIdAndDeletedAtIsNull(id, agentId)).thenReturn(Optional.empty());

			// when & then
			assertThatThrownBy(() -> customerReader.findByIdAndDeletedAtIsNotNullOrThrow(id, agentId))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("고객이 삭제되었거나 존재하지 않습니다.");
		}
	}

	@Nested
	@DisplayName("getCustomerByContact")
	class getCustomerByContactTest {
		@Test
		@DisplayName("고객이 존재할 때 정상 반환")
		void getCustomerByContact_success() {
			// given
			String contact = "010-1234-1234";
			Long agentId = 2L;
			Customer customer = Customer.builder()
				.contact(contact)
				.agent(Agent.builder().id(agentId).build())
				.build();
			when(customerRepository.findByContactAndAgentIdAndDeletedAtIsNull(contact, agentId)).thenReturn(
				Optional.of(customer));

			// when
			Customer result = customerReader.findByContactOrThrow(contact, agentId);

			// then
			assertThat(result).isEqualTo(customer);
		}

		@Test
		@DisplayName("고객이 없을 때 예외 발생")
		void getCustomerByContact_notFound() {
			// given
			String contact = "010-1234-1234";
			Long agentId = 2L;
			when(customerRepository.findByContactAndAgentIdAndDeletedAtIsNull(contact, agentId)).thenReturn(
				Optional.empty());

			// when,then
			assertThatThrownBy(() -> customerReader.findByContactOrThrow(contact, agentId))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("해당 전화번호로(" + contact + ")로 생성되었던 계정이 존재하지 않습니다.");
		}
	}

	@Nested
	@DisplayName("checkCustomer")
	class checkCustomerTest {
		@Test
		@DisplayName("고객이 존재하는 경우")
		void checkCustomer_success() {
			// given
			String contact = "010-1234-1234";
			Long agentId = 2L;
			Customer customer = Customer.builder()
				.contact(contact)
				.agent(Agent.builder().id(agentId).build())
				.build();

			when(customerRepository.findByContactAndAgentIdAndDeletedAtIsNull(contact, agentId))
				.thenReturn(Optional.of(customer));

			// when & then
			assertThatThrownBy(() -> customerReader.checkDuplicatedByContact(contact, agentId))
				.isInstanceOf(AlreadyExistsException.class)
				.hasMessageContaining("해당 전화번호로(" + contact + ")로 생성되었던 고객이 이미 존재합니다.") // ✅ "계정" → "고객" 수정
				.hasFieldOrPropertyWithValue("code", "CUSTOMER_ALREADY_EXIST");

			// verify
			verify(customerRepository, times(1))
				.findByContactAndAgentIdAndDeletedAtIsNull(contact, agentId);
		}

		@Test
		@DisplayName("존재하지 않는 고객 전화번호인 경우 예외 없음")
		void whenCustomerNotExists_noExceptionThrown() {
			// given
			String contact = "010-9876-5432";
			Long agentId = 2L;

			when(customerRepository.findByContactAndAgentIdAndDeletedAtIsNull(eq(contact), eq(agentId)))
				.thenReturn(Optional.empty());

			// when & then
			assertThatCode(() -> customerReader.checkDuplicatedByContact(contact, agentId))
				.doesNotThrowAnyException();

			// verify
			verify(customerRepository, times(1))
				.findByContactAndAgentIdAndDeletedAtIsNull(contact, agentId);
		}
	}

	@Nested
	@DisplayName("getAllCustomer")
	class getAllCustomerTest {
		@Test
		@DisplayName("고객이 존재하는 경우")
		void getAllCustomer_success() {
			// given
			String keyword = "홍길동";
			Long agentId = 2L;
			Pageable pageable = PageRequest.of(0, 10);
			boolean includeDeleted = false; // ✅ 추가된 파라미터
			Customer customer = Customer.builder().id(1L).agent(Agent.builder().id(agentId).build()).build();
			Page<Customer> mockPage = new PageImpl<>(Collections.singletonList(customer));

			// ✅ includeDeleted 파라미터 추가
			when(customerRepository.findAllByAgentIdAndFiltersAndDeletedOnly(
				agentId, keyword, keyword, keyword, includeDeleted, pageable))
				.thenReturn(mockPage);

			// when (includeDeleted 추가)
			Page<Customer> result = customerReader.findAllByKeyword(keyword, agentId, pageable, includeDeleted);

			// then (검증 로직 동일)
			assertThat(result.getContent().size()).isEqualTo(1);
			assertThat(result.getContent().get(0)).isEqualTo(customer);

			// verify (파라미터 개수 일치 확인)
			verify(customerRepository, times(1))
				.findAllByAgentIdAndFiltersAndDeletedOnly(agentId, keyword, keyword, keyword, includeDeleted,
					pageable);
		}
	}

}
