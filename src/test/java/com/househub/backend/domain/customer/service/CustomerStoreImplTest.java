package com.househub.backend.domain.customer.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.househub.backend.common.enums.Gender;
import com.househub.backend.common.exception.InvalidExcelValueException;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.common.response.ErrorResponse;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.customer.dto.CreateCustomerReqDto;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.customer.service.Impl.CustomerStoreImpl;
import com.househub.backend.domain.customer.util.ExcelParserUtils;

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
	void createCustomer_success() {
		// given
		Customer expectedCustomer = validRequest.toEntity(agent);
		when(customerRepository.save(any(Customer.class))).thenReturn(expectedCustomer);

		// when
		Customer result = customerStore.createCustomer(expectedCustomer);

		// then
		assertThat(result).isEqualTo(expectedCustomer);

		// verify
		verify(customerRepository, times(1)).save(any(Customer.class));
	}

	// 2. updateCustomer 테스트
	@Test
	@DisplayName("고객 정보 업데이트 성공")
	void updateCustomer_success() {
		// given
		Customer existingCustomer = Customer.builder()
			.id(1L)
			.contact("010-0000-0000")
			.agent(agent)
			.build();
		when(customerRepository.findByIdAndAgentIdAndDeletedAtIsNull(1L, 1L))
			.thenReturn(Optional.of(existingCustomer));
		when(customerRepository.save(any(Customer.class))).thenReturn(existingCustomer);

		// when
		Customer result = customerStore.updateCustomer(1L, validRequest, agent);

		// then
		assertThat(result.getContact()).isEqualTo(validRequest.getContact());
	}

	@Test
	@DisplayName("존재하지 않는 고객 업데이트 시도")
	void updateCustomer_notFound() {
		// given
		when(customerRepository.findByIdAndAgentIdAndDeletedAtIsNull(anyLong(), anyLong()))
			.thenReturn(Optional.empty());

		// when & then
		assertThatThrownBy(() -> customerStore.updateCustomer(999L, validRequest, agent))
			.isInstanceOf(ResourceNotFoundException.class);
	}

	// 3. deleteCustomer 테스트
	@Test
	@DisplayName("고객 소프트 삭제 성공")
	void deleteCustomer_success() {
		// given
		Customer customer = Customer.builder().id(1L).build();
		when(customerRepository.findByIdAndAgentIdAndDeletedAtIsNull(1L, 1L))
			.thenReturn(Optional.of(customer));

		// ✅ save() Mock 설정 추가
		when(customerRepository.save(any(Customer.class))).thenReturn(customer);

		ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(Customer.class);

		// when
		Customer result = customerStore.deleteCustomer(1L, agent);

		// then
		verify(customerRepository).save(captor.capture());
		assertThat(captor.getValue().getDeletedAt()).isNotNull();
	}


	@Test
	@DisplayName("엑셀 유효성 검증 실패 시 저장 로직 미호출")
	void createCustomersByExcel_validationFailed() {
		// given
		ExcelParserUtils.ExcelParseResult<CreateCustomerReqDto> mockResult = new ExcelParserUtils.ExcelParseResult<>(
			Collections.emptyList(),
			List.of(ErrorResponse.FieldError
				.builder()
				.field("ageGroup")
				.message("Invalid value")
				.build())
		);

		try (MockedStatic<ExcelParserUtils> mockedParser = mockStatic(ExcelParserUtils.class)) {
			mockedParser.when(() -> ExcelParserUtils.parseExcel(any(), any()))
				.thenReturn(mockResult);

			// when & then
			assertThatThrownBy(() -> customerStore.createCustomersByExcel(file, agent))
				.isInstanceOf(InvalidExcelValueException.class);

			// 저장 메서드 호출되지 않음 확인
			verify(customerRepository, never()).save(any());
		}
	}

}
