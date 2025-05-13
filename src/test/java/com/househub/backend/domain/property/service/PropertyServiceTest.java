package com.househub.backend.domain.property.service;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.contract.service.ContractStore;
import com.househub.backend.domain.customer.service.CustomerReader;
import com.househub.backend.domain.property.dto.CreatePropertyReqDto;
import com.househub.backend.domain.property.validator.PropertyValidator;
import com.househub.backend.domain.tag.entity.Tag;
import com.househub.backend.domain.tag.service.TagReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.repository.PropertyRepository;
import com.househub.backend.domain.property.service.impl.PropertyServiceImpl;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {
	@Mock
	private ContractStore contractStore;

	@Mock
	private CustomerReader customerReader;

	@Mock
	private PropertyStore propertyStore;

	@Mock
	private PropertyReader propertyReader;

	@Mock
	private PropertyTagMapStore propertyTagMapStore;

	@Mock
	private TagReader tagReader;

	@Mock
	private PropertyValidator propertyValidator;

	@InjectMocks
	private PropertyServiceImpl propertyService;

	private CreatePropertyReqDto createPropertyReqDto;
	private AgentResDto agentResDto;
	private Agent agent;
	private Customer customer;
	private Property property;
	private List<Tag> tags;

	@BeforeEach
	void setUp() {
		// 테스트 데이터 초기화
		Long propertyId = 1L;
		Long agentId = 100L;
		Long customerId = 200L;

		agent = Agent.builder()
				.id(agentId)
				.name("테스트 공인중개사")
				.build();

		agentResDto = AgentResDto.builder()
				.id(agentId)
				.name("테스트 공인중개사")
				.build();

		customer = Customer.builder()
				.id(customerId)
				.name("테스트 고객")
				.contact("010-1234-5678")
				.build();

		property = Property.builder()
				.id(propertyId)
				.customer(customer)
				.agent(agent)
				.roadAddress("서울시 강남구 테헤란로 123")
				.jibunAddress("서울시 강남구 역삼동 123-45")
				.deposit(50000000L)
				.monthlyRent(1500000L)
				.build();

		tags = Arrays.asList(
				Tag.builder().id(1L).name("신축").build(),
				Tag.builder().id(2L).name("역세권").build()
		);

		// 요청 DTO 생성
		createPropertyReqDto = CreatePropertyReqDto.builder()
				.customerId(customerId)
				.roadAddress("서울시 강남구 테헤란로 123")
				.jibunAddress("서울시 강남구 역삼동 123-45")
				.deposit(50000000L)
				.monthlyRent(1500000L)
				.tagIds(Arrays.asList(1L, 2L))
				.contract(BasicContractReqDto.builder()
						.deposit(50000000L)
						.monthlyRent(1500000L)
						.build())
				.build();
	}

	@Test
	@DisplayName("매물 등록 성공 테스트")
	void createProperty_Success() {
		// given
		Long expectedPropertyId = 1L;

		when(customerReader.findByIdAndDeletedAtIsNotNullOrThrow(anyLong(), anyLong()))
				.thenReturn(customer);
		when(propertyStore.create(any(Property.class)))
				.thenReturn(property);
		when(tagReader.findAllById(anyList()))
				.thenReturn(tags);

		// when
		CreatePropertyResDto result = propertyService.createProperty(createPropertyReqDto, agentResDto);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getPropertyId()).isEqualTo(expectedPropertyId);

		// 각 메서드 호출 검증
		verify(customerReader).findByIdAndDeletedAtIsNotNullOrThrow(createPropertyReqDto.getCustomerId(), agentResDto.getId());
		verify(propertyValidator).validateUniqueAddressForCustomer(
				createPropertyReqDto.getRoadAddress(),
				createPropertyReqDto.getJibunAddress(),
				createPropertyReqDto.getCustomerId()
		);
		verify(propertyStore).create(any(Property.class));
		verify(tagReader).findAllById(createPropertyReqDto.getTagIds());
		verify(propertyStore).addTag(any(Property.class), anyList());
		verify(contractStore).create(any());
	}

	@Test
	@DisplayName("계약 정보 없이 매물 등록 테스트")
	void createProperty_WithoutContract_Success() {
		// given
		Long expectedPropertyId = 1L;
		CreatePropertyReqDto dtoWithoutContract = CreatePropertyReqDto.builder()
				.customerId(createPropertyReqDto.getCustomerId())
				.roadAddress(createPropertyReqDto.getRoadAddress())
				.jibunAddress(createPropertyReqDto.getJibunAddress())
				.deposit(createPropertyReqDto.getDeposit())
				.monthlyRent(createPropertyReqDto.getMonthlyRent())
				.tagIds(createPropertyReqDto.getTagIds())
				.contract(null) // 계약 정보 없음
				.build();

		when(customerReader.findByIdAndDeletedAtIsNotNullOrThrow(anyLong(), anyLong()))
				.thenReturn(customer);
		when(propertyStore.create(any(Property.class)))
				.thenReturn(property);
		when(tagReader.findAllById(anyList()))
				.thenReturn(tags);

		// when
		CreatePropertyResDto result = propertyService.createProperty(dtoWithoutContract, agentResDto);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getPropertyId()).isEqualTo(expectedPropertyId);

		// 각 메서드 호출 검증
		verify(customerReader).findByIdAndDeletedAtIsNotNullOrThrow(dtoWithoutContract.getCustomerId(), agentResDto.getId());
		verify(propertyValidator).validateUniqueAddressForCustomer(
				dtoWithoutContract.getRoadAddress(),
				dtoWithoutContract.getJibunAddress(),
				dtoWithoutContract.getCustomerId()
		);
		verify(propertyStore).create(any(Property.class));
		verify(tagReader).findAllById(dtoWithoutContract.getTagIds());
		verify(propertyStore).addTag(any(Property.class), anyList());
		verify(contractStore, times(0)).create(any()); // 계약 생성 메서드가 호출되지 않아야 함
	}

	@Test
	@DisplayName("중복 주소 검증 실패 테스트")
	void createProperty_DuplicateAddress_ValidatorCalled() {
		// given
		when(customerReader.findByIdAndDeletedAtIsNotNullOrThrow(anyLong(), anyLong()))
				.thenReturn(customer);

		// when
		propertyService.createProperty(createPropertyReqDto, agentResDto);

		// then
		verify(propertyValidator).validateUniqueAddressForCustomer(
				createPropertyReqDto.getRoadAddress(),
				createPropertyReqDto.getJibunAddress(),
				createPropertyReqDto.getCustomerId()
		);
	}
}
