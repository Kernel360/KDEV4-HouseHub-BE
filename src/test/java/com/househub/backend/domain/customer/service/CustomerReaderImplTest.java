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
import com.househub.backend.domain.customer.service.Impl.CustomerReaderImpl;

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
			when(customerRepository.findByIdAndAgentIdAndDeletedAtIsNull(id, agentId)).thenReturn(Optional.of(customer));

			// when
			Customer result = customerReader.getCustomerById(id, agentId);

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
			assertThatThrownBy(() -> customerReader.getCustomerById(id, agentId))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("해당 ID로(" + id + ")로 생성된 계정이 존재하지 않습니다.");
		}
	}

	@Nested
	@DisplayName("getCustomerByContact")
	class getCustomerByContactTest {
		@Test
		@DisplayName("고객이 존재할 때 정상 반환")
		void getCustomerByContact_success(){
			// given
			String contact = "010-1234-1234";
			Long agentId = 2L;
			Customer customer = Customer.builder()
				.contact(contact)
				.agent(Agent.builder().id(agentId).build())
				.build();
			when(customerRepository.findByContactAndAgentIdAndDeletedAtIsNull(contact, agentId)).thenReturn(Optional.of(customer));

			// when
			Customer result = customerReader.getCustomerByContact(contact, agentId);

			// then
			assertThat(result).isEqualTo(customer);
		}

		@Test
		@DisplayName("고객이 없을 때 예외 발생")
		void getCustomerByContact_notFound(){
			// given
			String contact = "010-1234-1234";
			Long agentId = 2L;
			when(customerRepository.findByContactAndAgentIdAndDeletedAtIsNull(contact, agentId)).thenReturn(Optional.empty());

			// when,then
			assertThatThrownBy(() -> customerReader.getCustomerByContact(contact, agentId))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("해당 전화번호로(" + contact + ")로 생성되었던 계정이 존재하지 않습니다.");
		}
	}

	@Nested
	@DisplayName("checkCustomer")
	class checkCustomerTest {
		@Test
		@DisplayName("고객이 존재하는 경우")
		void checkCustomer_success(){
			// given
			String contact = "010-1234-1234";
			Long agentId = 2L;
			Customer customer = Customer.builder()
				.contact(contact)
				.agent(Agent.builder().id(agentId).build())
				.build();
			when(customerRepository.findByContactAndAgentIdAndDeletedAtIsNull(contact,agentId)).thenReturn(Optional.of(customer));

			// when & then
			assertThatThrownBy(() -> customerReader.checkCustomer(contact, agentId))
				.isInstanceOf(AlreadyExistsException.class)
				.hasMessageContaining("해당 전화번호로(" + contact + ")로 생성되었던 계정이 이미 존재합니다.")
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
			assertThatCode(() -> customerReader.checkCustomer(contact, agentId))
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
			Pageable pageable = PageRequest.of(0,10);
			Customer customer = Customer.builder().id(1L).agent(Agent.builder().id(agentId).build()).build();
			Page<Customer> mockPage = new PageImpl<>(Collections.singletonList(customer));

			when(customerRepository.findAllByAgentIdAndFiltersAndDeletedAtIsNull(agentId,keyword,keyword,keyword,pageable)).thenReturn(
				mockPage);

			// when
			Page<Customer> result = customerReader.getAllCustomer(keyword, agentId, pageable);

			// then
			assertThat(result.getContent().size()).isEqualTo(1);
			assertThat(result.getContent().get(0)).isEqualTo(customer);

			// verify
			verify(customerRepository, times(1)).findAllByAgentIdAndFiltersAndDeletedAtIsNull(agentId, keyword, keyword,keyword,pageable);
		}
	}
}
