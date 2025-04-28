// package com.househub.backend.domain.customer.service;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.*;
//
// import java.time.LocalDate;
// import java.util.Optional;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// import com.househub.backend.common.enums.Gender;
// import com.househub.backend.domain.agent.entity.Agent;
// import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
// import com.househub.backend.domain.customer.entity.Customer;
// import com.househub.backend.domain.customer.repository.CustomerRepository;
//
// @ExtendWith(MockitoExtension.class)
// class CustomerStoreImplTest {
//
// 	@Mock
// 	private CustomerRepository customerRepository;
//
// 	@InjectMocks
// 	private CustomerStoreImpl customerStore;
//
// 	private Agent agent;
// 	private CreateCustomerReqDto reqDto;
//
// 	@BeforeEach
// 	void setUp() {
// 		agent = Agent.builder()
// 			.id(1L)
// 			.name("테스트 에이전트")
// 			.build();
//
// 		reqDto = CreateCustomerReqDto.builder()
// 			.name("테스트 고객")
// 			.birthDate(LocalDate.of(1990, 1, 1))
// 			.contact("010-1234-5678")
// 			.email("test@test.com")
// 			.memo("테스트 메모")
// 			.gender(Gender.M)
// 			.build();
// 	}
//
// 	@Test
// 	void createCustomer_성공() {
// 		// given
// 		Customer customer = reqDto.toEntity(agent);
// 		when(customerRepository.save(any(Customer.class))).thenReturn(customer);
//
// 		// when
// 		Customer result = customerStore.createCustomer(reqDto, agent);
//
// 		// then
// 		assertNotNull(result);
// 		assertEquals(reqDto.getName(), result.getName());
// 		assertEquals(reqDto.getBirthDate(), result.getBirthDate());
// 		assertEquals(reqDto.getContact(), result.getContact());
// 		assertEquals(reqDto.getEmail(), result.getEmail());
// 		assertEquals(reqDto.getMemo(), result.getMemo());
// 		assertEquals(reqDto.getGender(), result.getGender());
// 		verify(customerRepository, times(1)).save(any(Customer.class));
// 	}
//
// 	@Test
// 	void findById_성공() {
// 		// given
// 		Customer customer = reqDto.toEntity(agent);
// 		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
//
// 		// when
// 		Customer result = customerStore.findById(1L);
//
// 		// then
// 		assertNotNull(result);
// 		assertEquals(customer.getName(), result.getName());
// 		verify(customerRepository, times(1)).findById(1L);
// 	}
//
// 	@Test
// 	void findById_실패() {
// 		// given
// 		when(customerRepository.findById(1L)).thenReturn(Optional.empty());
//
// 		// when & then
// 		assertThrows(IllegalArgumentException.class, () -> customerStore.findById(1L));
// 		verify(customerRepository, times(1)).findById(1L);
// 	}
// }
