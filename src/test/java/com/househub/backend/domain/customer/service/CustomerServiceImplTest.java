package com.househub.backend.domain.customer.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.List;

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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.dto.CreateCustomerResDto;
import com.househub.backend.domain.customer.dto.CustomerListResDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.Impl.CustomerServiceImpl;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

	@Mock
	private CustomerStore customerStore;

	@Mock
	private CustomerReader customerReader;

	@InjectMocks
	private CustomerServiceImpl customerService;

	private Agent testAgent;
	private CreateCustomerReqDto testRequest;

	@BeforeEach
	void setUp() {
		testAgent = Agent.builder()
			.id(1L)
			.name("Test Agent")
			.build();

		testRequest = CreateCustomerReqDto.builder()
			.name("김철수")
			.ageGroup(20)
			.contact("010-1234-5678")
			.email("test@example.com")
			.memo("서울시 강남구")
			.gender(Gender.M)
			.build();
	}

	@Test
	@DisplayName("고객 생성 성공")
	void createCustomer_success() {
		// given
		Agent agent = Agent.builder().id(1L).build();
		AgentResDto agentDto = AgentResDto.fromEntity(agent);

		Customer savedCustomer = Customer.builder()
			.id(1L)
			.name(testRequest.getName())
			.contact(testRequest.getContact())
			.ageGroup(testRequest.getAgeGroup())
			.email(testRequest.getEmail())
			.memo(testRequest.getMemo())
			.gender(testRequest.getGender())
			.agent(agent)
			.build();

		// customerReader.checkCustomer()는 아무 동작도 하지 않음(중복 아님)
		doNothing().when(customerReader).checkCustomer(testRequest.getContact(), agent.getId());
		// 저장 시 저장된 고객 리턴
		when(customerStore.createCustomer(any(Customer.class))).thenReturn(savedCustomer);

		// when
		CreateCustomerResDto result = customerService.createCustomer(testRequest, agentDto);

		// then
		assertThat(result.getId()).isEqualTo(savedCustomer.getId());
		assertThat(result.getName()).isEqualTo(savedCustomer.getName());
		assertThat(result.getContact()).isEqualTo(savedCustomer.getContact());

		verify(customerReader).checkCustomer(testRequest.getContact(), agent.getId());
		verify(customerStore).createCustomer(any(Customer.class));
	}

	@Test
	@DisplayName("고객 생성 시 중복 전화번호 예외 발생")
	void createCustomer_duplicateContact() {
		// given
		Agent agent = Agent.builder().id(1L).build();
		AgentResDto agentDto = AgentResDto.fromEntity(agent);

		CreateCustomerReqDto reqDto = CreateCustomerReqDto.builder()
			.contact("010-1234-5678")
			.build();

		doThrow(new AlreadyExistsException("중복", "CODE"))
			.when(customerReader).checkCustomer(reqDto.getContact(), agent.getId());

		// when & then
		assertThatThrownBy(() -> customerService.createCustomer(reqDto, agentDto))
			.isInstanceOf(AlreadyExistsException.class);

		verify(customerReader).checkCustomer(reqDto.getContact(), agent.getId());
		verify(customerStore, never()).createCustomer(any());
	}

	@Test
	@DisplayName("엑셀 일괄 등록 테스트")
	void createCustomersByExcel_Success() throws IOException {
		// Given
		MultipartFile mockFile = new MockMultipartFile("test.xlsx", new byte[0]);
		List<Customer> mockCustomers = List.of(testRequest.toEntity(testAgent));
		when(customerStore.createCustomersByExcel(any(), any())).thenReturn(mockCustomers);

		// When
		List<CreateCustomerResDto> results = customerService.createCustomersByExcel(
			mockFile,
			AgentResDto.fromEntity(testAgent)
		);

		// Then
		assertThat(results).hasSize(1);
		assertThat(results.get(0).getEmail()).isEqualTo("test@example.com");
	}

	@Test
	@DisplayName("페이징 고객 목록 조회 테스트")
	void findAll_Pagination() {
		// Given
		Page<Customer> mockPage = new PageImpl<>(List.of(testRequest.toEntity(testAgent)));
		when(customerReader.getAllCustomer(any(), any(), any())).thenReturn(mockPage);

		// When
		CustomerListResDto result = customerService.findAll(
			"검색어",
			AgentResDto.fromEntity(testAgent),
			PageRequest.of(0, 10)
		);

		// Then
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getPagination().getTotalPages()).isEqualTo(1);
	}

	@Test
	@DisplayName("고객 단건 조회 테스트")
	void findById_Success() {
		// Given
		Customer foundCustomer = Customer.builder()
			.id(1L)
			.agent(testAgent)
			.name(testRequest.getName())
			.contact(testRequest.getContact())
			.email(testRequest.getEmail())
			.memo(testRequest.getMemo())
			.build();
		when(customerReader.getCustomerById(eq(1L), eq(testAgent.getId()))).thenReturn(foundCustomer);

		// When
		CreateCustomerResDto result = customerService.findById(
			1L,
			AgentResDto.fromEntity(testAgent)
		);

		// Then
		assertThat(result.getId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("고객 정보 수정 테스트")
	void updateCustomer_Success() {
		// Given
		Customer updatedCustomer = testRequest.toEntity(testAgent);
		when(customerStore.updateCustomer(any(Long.class), any(CreateCustomerReqDto.class),
			any(Agent.class))).thenReturn(updatedCustomer);

		// When
		CreateCustomerResDto result = customerService.updateCustomer(
			1L,
			testRequest,
			AgentResDto.fromEntity(testAgent)
		);

		// Then
		assertThat(result.getMemo()).isEqualTo("서울시 강남구");
	}

	@Test
	@DisplayName("고객 정보 삭제 테스트")
	void deleteCustomer_Success() {
		// Given
		Customer deletedCustomer = testRequest.toEntity(testAgent);
		when(customerStore.deleteCustomer(eq(1L), any(Agent.class)))
			.thenReturn(deletedCustomer);

		// When
		CreateCustomerResDto result = customerService.deleteCustomer(
			1L,
			AgentResDto.fromEntity(testAgent)
		);

		// Then
		assertThat(result.getMemo()).isEqualTo("서울시 강남구");
		verify(customerStore, times(1))
			.deleteCustomer(eq(1L), any(Agent.class));
	}
}
