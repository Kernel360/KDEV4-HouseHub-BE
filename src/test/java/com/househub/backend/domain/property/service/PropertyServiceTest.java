package com.househub.backend.domain.property.service;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.property.dto.PropertyReqDto;
import com.househub.backend.domain.property.dto.PropertyUpdateReqDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.service.impl.PropertyServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {
	@InjectMocks
	private PropertyServiceImpl propertyService;

	@Mock
	private PropertyReader propertyReader;

	@Mock
	private PropertyStore propertyStore;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AgentRepository agentRepository;

	@Mock
	private PropertyConditionService propertyConditionService;

	private Agent mockAgent;
	private Customer mockCustomer;
	private Property mockProperty;

	@BeforeEach
	void setUp() {
		mockAgent = Agent.builder().id(1L).status(AgentStatus.ACTIVE).build();
		mockCustomer = Customer.builder().id(1L).build();
		mockProperty = Property.builder()
			.id(1L)
			.customer(mockCustomer)
			.agent(mockAgent)
			.roadAddress("서울시 도로명")
			.detailAddress("상세 주소")
			.build();
	}

	@Test
	void createPropertyWithoutConditions() {
		// given
		Long agentId = 1L;
		Long customerId = 2L;

		PropertyReqDto reqDto = PropertyReqDto.builder()
			.customerId(customerId)
			.roadAddress("서울시 강남구 테헤란로 123")
			.jibunAddress("서울시 강남구 서초동 123-45")
			.detailAddress("101동 101호")
			// .conditions(List.of(new PropertyConditionReqDto(...))) // 적절한 값 넣기
            .build();

		Agent agent = Agent.builder().id(agentId).build();
		Customer customer = Customer.builder().id(customerId).build();
		Property property = mock(Property.class); // 또는 reqDto.toEntity(customer, agent)

		given(agentRepository.findByIdAndStatus(agentId, AgentStatus.ACTIVE))
			.willReturn(Optional.of(agent));
		given(customerRepository.findById(customerId))
			.willReturn(Optional.of(customer));
		willDoNothing().given(propertyReader)
			.validateRegisterProperty(anyString(), anyString(), eq(customerId));
		given(propertyStore.store(any(Property.class))).willReturn(property);

		// when
		propertyService.createProperty(reqDto, agentId);

		// then
		verify(agentRepository).findByIdAndStatus(agentId, AgentStatus.ACTIVE);
		verify(customerRepository).findById(customerId);
		verify(propertyReader).validateRegisterProperty(any(), any(), any());
		verify(propertyStore).store(any(Property.class));
		// verify(propertyConditionService).createPropertyCondition(anyLong(), any(PropertyConditionReqDto.class));
	}

	@Test
	void updatePropertySuccess() {
		// given
		Long propertyId = 1L;
		PropertyUpdateReqDto updateDto = PropertyUpdateReqDto.builder()
			.customerId(1L)
			.roadAddress("대전 동구 옥천로 123")
			.jibunAddress("대전 동구 옥천동 123-45")
			.detailAddress("변경된 상세주소")
			.build();

		when(propertyReader.findPropertyBy(propertyId)).thenReturn(mockProperty);
		when(customerRepository.findById(1L)).thenReturn(Optional.of(mockCustomer));

		// when
		propertyService.updateProperty(propertyId, updateDto);

		// then
		verify(propertyReader).validateRegisterProperty(anyString(), anyString(), anyLong());
		verify(customerRepository).findById(1L);
	}
}
