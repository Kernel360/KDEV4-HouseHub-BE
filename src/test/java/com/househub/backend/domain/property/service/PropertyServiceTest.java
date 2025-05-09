package com.househub.backend.domain.property.service;

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
	//
	// @BeforeEach
	// void setUp() {
	// 	agent = Agent.builder()
	// 		.id(1L)
	// 		.status(AgentStatus.ACTIVE)
	// 		.build();
	//
	// 	customer = Customer.builder()
	// 		.id(1L)
	// 		.build();
	//
	// 	property = Property.builder()
	// 		.id(1L)
	// 		.agent(agent)
	// 		.customer(customer)
	// 		.contracts(new ArrayList<>())
	// 		.build();
	// }
	//
	// @Test
	// @DisplayName("매물 등록 성공")
	// void createProperty_Success() {
	// 	CreatePropertyReqDto dto = CreatePropertyReqDto.builder()
	// 		.customerId(1L)
	// 		.roadAddress("서울특별시 강남구 테헤란로 123")
	// 		.jibunAddress("서울특별시 강남구 삼성동 123-45")
	// 		.detailAddress("101동 302호")
	// 		.build();
	// 	when(agentRepository.findByIdAndStatus(1L, AgentStatus.ACTIVE)).thenReturn(Optional.of(agent));
	// 	when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
	// 	when(propertyRepository.existsByRoadAddressAndDetailAddress(dto.getRoadAddress(),
	// 		dto.getDetailAddress())).thenReturn(false);
	// 	when(propertyRepository.save(any(Property.class))).thenReturn(property);
	//
	// 	// 매물 등록 메서드 호출
	// 	CreatePropertyResDto response = propertyService.createProperty(dto, 1L);
	//
	// 	assertNotNull(response);
	// }
	//
	// @Test
	// @DisplayName("동일한 주소를 가진 매물을 등록할 경우 - 예외 처리")
	// void createProperty_ThrowsAlreadyExistsException() {
	// 	CreatePropertyReqDto dto = CreatePropertyReqDto.builder()
	// 		.customerId(1L)
	// 		.propertyType(PropertyType.APARTMENT)
	// 		.roadAddress("서울특별시 강남구 테헤란로 123")
	// 		.jibunAddress("서울특별시 강남구 삼성동 123-45")
	// 		.detailAddress("101동 302호")
	// 		.build();
	// 	when(agentRepository.findByIdAndStatus(1L, AgentStatus.ACTIVE)).thenReturn(Optional.of(agent));
	// 	when(propertyRepository.existsByRoadAddressAndDetailAddress(dto.getRoadAddress(),
	// 		dto.getDetailAddress())).thenReturn(true);
	//
	// 	assertThrows(AlreadyExistsException.class, () -> propertyService.createProperty(dto, 1L));
	// }
	//
	// @Test
	// @DisplayName("매물 상세 조회 성공")
	// void findProperty_Success() {
	// 	when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
	//
	// 	FindPropertyDetailResDto response = propertyService.findProperty(1L);
	//
	// 	assertNotNull(response);
	// 	assertEquals(1L, response.getId());
	// }
	//
	// @Test
	// @DisplayName("존재하지 않는 매물인 경우 - 예외 처리")
	// void findProperty_ThrowsResourceNotFoundException() {
	// 	when(propertyRepository.findById(1L)).thenReturn(Optional.empty());
	//
	// 	assertThrows(ResourceNotFoundException.class, () -> propertyService.findProperty(1L));
	// }
	//
	// @Test
	// @DisplayName("전체 매물 조회 성공")
	// void findProperties_Success() {
	// 	Long agentId = 1L;
	//
	// 	PropertySearchDto searchDto = PropertySearchDto.builder()
	// 		.province("서울")
	// 		.city("강남구")
	// 		.dong("역삼동")
	// 		.propertyType(PropertyType.APARTMENT)
	// 		.agentName("홍길동")
	// 		.customerName("김철수")
	// 		.active(true)
	// 		.build();
	//
	// 	Pageable pageable = PageRequest.of(0, 10);
	//
	// 	Property mockProperty = Property.builder()
	// 		.id(1L)
	// 		.propertyType(PropertyType.APARTMENT)
	// 		.build();
	//
	// 	Page<Property> propertyPage = new PageImpl<>(List.of(mockProperty), pageable, 1);
	//
	// 	when(propertyRepository.searchProperties(
	// 		eq(agentId),
	// 		eq("서울"),
	// 		eq("강남구"),
	// 		eq("역삼동"),
	// 		eq(PropertyType.APARTMENT),
	// 		eq("홍길동"),
	// 		eq("김철수"),
	// 		eq(true),
	// 		eq(pageable)
	// 	)).thenReturn(propertyPage);
	//
	// 	// when
	// 	PropertyListResDto result = propertyService.findProperties(searchDto, pageable, agentId);
	//
	// 	// then
	// 	assertThat(result).isNotNull();
	//
	// 	verify(propertyRepository, times(1)).searchProperties(
	// 		eq(agentId),
	// 		eq("서울"),
	// 		eq("강남구"),
	// 		eq("역삼동"),
	// 		eq(PropertyType.APARTMENT),
	// 		eq("홍길동"),
	// 		eq("김철수"),
	// 		eq(true),
	// 		eq(pageable)
	// 	);
	// }
	//
	// @Test
	// @DisplayName("매물 정보 수정 성공")
	// void updateProperty_Success() {
	// 	CreatePropertyReqDto updateDto = CreatePropertyReqDto.builder()
	// 		.customerId(1L)
	// 		.roadAddress("서울특별시 강남구 테헤란로 123")
	// 		.jibunAddress("서울특별시 강남구 삼성동 123-45")
	// 		.detailAddress("101동 302호")
	// 		.build();
	// 	// when(propertyRepository.existsByRoadAddressAndDetailAddress(updateDto.getRoadAddress(),
	// 	// 	updateDto.getDetailAddress())).thenReturn(false);
	// 	when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
	// 	when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
	//
	// 	propertyService.updateProperty(1L, updateDto);
	//
	// 	verify(propertyRepository, times(1)).findById(1L);
	// }
	//
	// @Test
	// @DisplayName("매물 삭제 성공")
	// void deleteProperty_Success() {
	// 	Contract contract1 = Contract.builder().id(1L).property(property).build();
	// 	Contract contract2 = Contract.builder().id(2L).property(property).build();
	// 	property = Property.builder()
	// 		.id(1L)
	// 		.contracts(List.of(contract1, contract2)) // 매물에 계약 2개 추가
	// 		.build();
	// 	when(propertyRepository.findById(1L)).thenReturn(Optional.of(property));
	//
	// 	// when: 매물 삭제 메서드 실행
	// 	propertyService.deleteProperty(1L);
	//
	// 	// then: 매물과 모든 계약이 소프트 삭제되었는지 검증
	// 	assertNotNull(property.getDeletedAt());
	// 	property.getContracts().forEach(contract -> assertNotNull(contract.getDeletedAt()));
	// }
}
