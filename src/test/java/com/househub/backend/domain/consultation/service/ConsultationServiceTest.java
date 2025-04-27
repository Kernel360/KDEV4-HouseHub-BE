package com.househub.backend.domain.consultation.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.consultation.dto.CreateConsultationReqDto;
import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.repository.ConsultationRepository;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class ConsultationServiceTest {

	@Mock
	private ConsultationRepository consultationRepository;

	@Mock
	private CustomerRepository customerRepository;

	@InjectMocks
	private ConsultationService consultationService;

	private Agent agent;
	private Customer customer;
	private CreateConsultationReqDto reqDto;

	@BeforeEach
	void setUp() {
		agent = Agent.builder()
			.id(1L)
			.name("테스트 에이전트")
			.build();

		customer = Customer.builder()
			.id(1L)
			.name("테스트 고객")
			.birthDate(LocalDate.of(1990, 1, 1))
			.contact("010-1234-5678")
			.email("test@test.com")
			.gender(Gender.M)
			.agent(agent)
			.build();

		reqDto = CreateConsultationReqDto.builder()
			.customerId(1L)
			.memo("테스트 상담 메모")
			.build();
	}

	@Test
	void createConsultation_성공() {
		// given
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		when(consultationRepository.save(any(Consultation.class))).thenAnswer(invocation -> invocation.getArgument(0));

		// when
		Consultation result = consultationService.createConsultation(reqDto, agent);

		// then
		assertNotNull(result);
		assertEquals(customer, result.getCustomer());
		assertEquals(agent, result.getAgent());
		assertEquals(reqDto.getMemo(), result.getMemo());
		verify(customerRepository, times(1)).findById(1L);
		verify(consultationRepository, times(1)).save(any(Consultation.class));
	}

	@Test
	void createConsultation_실패_고객없음() {
		// given
		when(customerRepository.findById(1L)).thenReturn(Optional.empty());

		// when & then
		assertThrows(IllegalArgumentException.class, () -> consultationService.createConsultation(reqDto, agent));
		verify(customerRepository, times(1)).findById(1L);
		verify(consultationRepository, never()).save(any(Consultation.class));
	}
}
