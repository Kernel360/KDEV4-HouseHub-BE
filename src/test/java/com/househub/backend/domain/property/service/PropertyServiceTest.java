package com.househub.backend.domain.property.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
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

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.entity.AgentStatus;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.property.dto.CreatePropertyResDto;
import com.househub.backend.domain.property.dto.FindPropertyDetailResDto;
import com.househub.backend.domain.property.dto.PropertyListResDto;
import com.househub.backend.domain.property.dto.PropertyReqDto;
import com.househub.backend.domain.property.dto.PropertySearchDto;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.enums.PropertyType;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.impl.PropertyServiceImpl;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {
	@InjectMocks
	private PropertyServiceImpl propertyService;

	@Mock
	private PropertyRepository propertyRepository;

	@Mock
	private CustomerRepository customerRepository;

	@Mock
	private AgentRepository agentRepository;

	private Agent agent;
	private Customer customer;
	private Property property;

	@BeforeEach
	void setUp() {
		agent = Agent.builder()
			.id(1L)
			.status(AgentStatus.ACTIVE)
			.build();

		customer = Customer.builder()
			.id(1L)
			.build();

		property = Property.builder()
			.id(1L)
			.agent(agent)
			.customer(customer)
			.contracts(new ArrayList<>())
			.build();
	}

	@Test
	@DisplayName("매물 등록 성공")
	void createProperty_Success() {
		PropertyReqDto dto = PropertyReqDto.builder()
			.customerId(1L)
			.roadAddress("서울특별시 강남구 테헤란로 123")
			.jibunAddress("서울특별시 강남구 삼성동 123-45")
			.detailAddress("101동 302호")
			.build();
		when(agentRepository.findByIdAndStatus(1L, AgentStatus.ACTIVE)).thenReturn(Optional.of(agent));
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		when(propertyRepository.existsByRoadAddressAndDetailAddress(dto.getRoadAddress(),
			dto.getDetailAddress())).thenReturn(false);
		when(propertyRepository.save(any(Property.class))).thenReturn(property);

		// 매물 등록 메서드 호출
		CreatePropertyResDto response = propertyService.createProperty(dto, 1L);

		assertNotNull(response);
	}

	@Test
	@DisplayName("동일한 주소를 가진 매물을 등록할 경우 - 예외 처리")
	void createProperty_ThrowsAlreadyExistsException() {
		PropertyReqDto dto = PropertyReqDto.builder()
			.customerId(1L)
			.propertyType(PropertyType.APARTMENT)
			.roadAddress("서울특별시 강남구 테헤란로 123")
			.jibunAddress("서울특별시 강남구 삼성동 123-45")
			.detailAddress("101동 302호")
			.build();
		when(agentRepository.findByIdAndStatus(1L, AgentStatus.ACTIVE)).thenReturn(Optional.of(agent));
		when(propertyRepository.existsByRoadAddressAndDetailAddress(dto.getRoadAddress(),
			dto.getDetailAddress())).thenReturn(true);

		assertThrows(AlreadyExistsException.class, () -> propertyService.createProperty(dto, 1L));
	}

	@Test
	@DisplayName("매물 상세 조회 성공")
	void findProperty_Success() {
		when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

		FindPropertyDetailResDto response = propertyService.findProperty(1L);

		assertNotNull(response);
		assertEquals(1L, response.getId());
	}

	@Test
	@DisplayName("존재하지 않는 매물인 경우 - 예외 처리")
	void findProperty_ThrowsResourceNotFoundException() {
		when(propertyRepository.findById(1L)).thenReturn(Optional.empty());

		assertThrows(ResourceNotFoundException.class, () -> propertyService.findProperty(1L));
	}

	@Test
	@DisplayName("전체 매물 조회 성공")
	void findProperties_Success() {
		PropertySearchDto searchDto = PropertySearchDto.builder().build();
		Pageable pageable = PageRequest.of(0, 10);
		Page<Property> page = new PageImpl<>(List.of(property), pageable, 1);

		when(propertyRepository.searchProperties(any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(page);

		PropertyListResDto response = propertyService.findProperties(searchDto, pageable);

		assertFalse(response.getContent().isEmpty());
	}

	@Test
	@DisplayName("매물 정보 수정 성공")
	void updateProperty_Success() {
		PropertyReqDto updateDto = PropertyReqDto.builder()
			.customerId(1L)
			.roadAddress("서울특별시 강남구 테헤란로 123")
			.jibunAddress("서울특별시 강남구 삼성동 123-45")
			.detailAddress("101동 302호")
			.build();
		// when(propertyRepository.existsByRoadAddressAndDetailAddress(updateDto.getRoadAddress(),
		// 	updateDto.getDetailAddress())).thenReturn(false);
		when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
		when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

		propertyService.updateProperty(1L, updateDto);

		verify(propertyRepository, times(1)).findById(1L);
	}

	@Test
	@DisplayName("매물 삭제 성공")
	void deleteProperty_Success() {
		Contract contract1 = Contract.builder().id(1L).property(property).build();
		Contract contract2 = Contract.builder().id(2L).property(property).build();
		property = Property.builder()
			.id(1L)
			.contracts(List.of(contract1, contract2)) // 매물에 계약 2개 추가
			.build();
		when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));

		// when: 매물 삭제 메서드 실행
		propertyService.deleteProperty(1L);

		// then: 매물과 모든 계약이 소프트 삭제되었는지 검증
		assertNotNull(property.getDeletedAt());
		property.getContracts().forEach(contract -> assertNotNull(contract.getDeletedAt()));
	}
}
