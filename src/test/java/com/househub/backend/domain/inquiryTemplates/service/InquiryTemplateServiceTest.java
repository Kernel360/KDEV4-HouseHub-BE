package com.househub.backend.domain.inquiryTemplates.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplateListResDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplatePreviewResDto;
import com.househub.backend.domain.inquiryForm.dto.UpdateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryForm.entity.Question;
import com.househub.backend.domain.inquiryForm.entity.QuestionType;
import com.househub.backend.domain.inquiryForm.repository.InquiryTemplateRepository;
import com.househub.backend.domain.inquiryForm.repository.QuestionRepository;
import com.househub.backend.domain.inquiryForm.service.impl.InquiryTemplateServiceImpl;

@ExtendWith(MockitoExtension.class)
public class InquiryTemplateServiceTest {
	@Mock
	private InquiryTemplateRepository inquiryTemplateRepository;

	@Mock
	private QuestionRepository questionRepository;

	@InjectMocks
	private InquiryTemplateServiceImpl inquiryTemplateService;

	private CreateInquiryTemplateReqDto reqDto;
	private InquiryTemplate inquiryTemplate;
	private List<Question> questions;

	// 테스트에 필요한 데이터 초기화
	@BeforeEach
	void setup() {
		// 테스트 데이터 생성
		CreateInquiryTemplateReqDto.QuestionDto questionDto1 = CreateInquiryTemplateReqDto.QuestionDto.builder()
			.label("이름")
			.type(QuestionType.TEXT)
			.required(true)
			.questionOrder(1)
			.build();

		CreateInquiryTemplateReqDto.QuestionDto questionDto2 = CreateInquiryTemplateReqDto.QuestionDto.builder()
			.label("이메일")
			.type(QuestionType.TEXT)
			.required(true)
			.questionOrder(2)
			.build();

		List<CreateInquiryTemplateReqDto.QuestionDto> questionDtos = Arrays.asList(questionDto1, questionDto2);

		reqDto = CreateInquiryTemplateReqDto.builder()
			.name("고객 문의 템플릿")
			.description("고객 문의 시 사용하는 템플릿입니다.")
			.questions(questionDtos)
			.isActive(true)
			.build();

		questions = reqDto.getQuestions().stream()
			.map(questionDto -> Question.fromDto(questionDto, inquiryTemplate))
			.collect(Collectors.toList());

		inquiryTemplate = InquiryTemplate.builder()
			.name(reqDto.getName())
			.description(reqDto.getDescription())
			.isActive(reqDto.isActive())
			.questions(questions)
			.build();
	}

	@Test
	@DisplayName("문의 템플릿 등록 성공")
	void createNewInquiryTemplate_success() {
		// given: 테스트에 필요한 객체와 데이터를 설정
		// DTO를 엔티티로 변환하는 로직 모방
		InquiryTemplate inquiryTemplate = InquiryTemplate.fromDto(reqDto);
		List<Question> questions = reqDto.getQuestions().stream()
			.map(questionDto -> Question.fromDto(questionDto, inquiryTemplate))
			.collect(Collectors.toList());

		// Mock 객체의 행위 정의
		when(inquiryTemplateRepository.save(any(InquiryTemplate.class))).thenReturn(inquiryTemplate);
		when(questionRepository.saveAll(anyList())).thenReturn(questions);

		// when: 테스트 대상 메서드를 실행합니다.
		inquiryTemplateService.createNewInquiryTemplate(reqDto);

		// then: 메서드 실행 결과 검증.
		verify(inquiryTemplateRepository, times(1)).save(any(InquiryTemplate.class));
		verify(questionRepository, times(1)).saveAll(anyList());
	}

	@Test
	@DisplayName("문의 템플릿 목록 조회 성공")
	void getInquiryTemplates_success() {
		// given: 테스트 데이터 준비
		InquiryTemplate template1 = InquiryTemplate.builder()
			.name("고객 문의 템플릿 1")
			.description("첫 번째 템플릿")
			.isActive(true)
			.build();

		InquiryTemplate template2 = InquiryTemplate.builder()
			.name("고객 문의 템플릿 2")
			.description("두 번째 템플릿")
			.isActive(true)
			.build();

		List<InquiryTemplate> templates = Arrays.asList(template1, template2);
		Page<InquiryTemplate> page = new PageImpl<>(templates);

		// pageable 객체 설정
		Pageable pageable = PageRequest.of(0, 10);

		// Mock 객체의 행위 정의
		when(inquiryTemplateRepository.findAllByFilters(anyBoolean(), any(Pageable.class))).thenReturn(page);

		// when: 서비스 메서드 호출
		InquiryTemplateListResDto result = inquiryTemplateService.getInquiryTemplates(true, pageable);

		// then: 결과 검증
		assertNotNull(result);
		assertEquals(2, result.getTemplates().size());
		assertEquals(1, result.getTotalPages());
		assertEquals(2, result.getTotalElements());
		assertEquals(2, result.getSize());
		assertEquals(0, result.getCurrentPage());

		// repository가 호출된 횟수 검증
		verify(inquiryTemplateRepository, times(1)).findAllByFilters(anyBoolean(), any(Pageable.class));
	}

	@Test
	@DisplayName("저장된 템플릿이 없을 때 빈 목록 반환")
	void getInquiryTemplates_emptyList() {
		// given: 빈 리스트 반환 설정
		Page<InquiryTemplate> emptyPage = Page.empty();
		Pageable pageable = PageRequest.of(0, 10);

		when(inquiryTemplateRepository.findAllByFilters(anyBoolean(), any(Pageable.class))).thenReturn(emptyPage);

		// when: 서비스 메서드 호출
		InquiryTemplateListResDto result = inquiryTemplateService.getInquiryTemplates(true, pageable);

		// then: 결과 검증
		assertNotNull(result);
		assertTrue(result.getTemplates().isEmpty());
		assertEquals(1, result.getTotalPages());
		assertEquals(0, result.getTotalElements());
		assertEquals(0, result.getSize());
		assertEquals(0, result.getCurrentPage());

		// repository가 호출된 횟수 검증
		verify(inquiryTemplateRepository, times(1)).findAllByFilters(anyBoolean(), any(Pageable.class));
	}

	@Test
	@DisplayName("키워드로 문의 템플릿 검색 성공")
	void searchInquiryTemplates_success() {
		// given: 테스트 데이터 준비
		InquiryTemplate template1 = InquiryTemplate.builder()
			.name("고객 문의 템플릿 1")
			.description("첫 번째 템플릿")
			.isActive(true)
			.build();

		InquiryTemplate template2 = InquiryTemplate.builder()
			.name("고객 문의 템플릿 2")
			.description("두 번째 템플릿")
			.isActive(true)
			.build();

		List<InquiryTemplate> templates = Arrays.asList(template1, template2);
		Page<InquiryTemplate> page = new PageImpl<>(templates);

		// pageable 객체 설정
		Pageable pageable = PageRequest.of(0, 10);

		// Mock 객체의 행위 정의
		when(inquiryTemplateRepository.findAllByKeyword(anyString(), any(Pageable.class))).thenReturn(page);

		// when: 서비스 메서드 호출
		InquiryTemplateListResDto result = inquiryTemplateService.searchInquiryTemplates("고객", pageable);

		// then: 결과 검증
		assertNotNull(result);
		assertEquals(2, result.getTemplates().size());
		assertEquals(1, result.getTotalPages());
		assertEquals(2, result.getTotalElements());
		assertEquals(2, result.getSize());
		assertEquals(0, result.getCurrentPage());

		// repository가 호출된 횟수 검증
		verify(inquiryTemplateRepository, times(1)).findAllByKeyword(anyString(), any(Pageable.class));
	}

	@Test
	@DisplayName("키워드 검색 결과가 없을 때 빈 목록 반환")
	void searchInquiryTemplates_emptyList() {
		// given: 빈 리스트 반환 설정
		Page<InquiryTemplate> emptyPage = Page.empty();
		Pageable pageable = PageRequest.of(0, 10);

		when(inquiryTemplateRepository.findAllByKeyword(anyString(), any(Pageable.class))).thenReturn(emptyPage);

		// when: 서비스 메서드 호출
		InquiryTemplateListResDto result = inquiryTemplateService.searchInquiryTemplates("없는 키워드", pageable);

		// then: 결과 검증
		assertNotNull(result);
		assertTrue(result.getTemplates().isEmpty());
		assertEquals(1, result.getTotalPages());
		assertEquals(0, result.getTotalElements());
		assertEquals(0, result.getSize());
		assertEquals(0, result.getCurrentPage());

		// repository가 호출된 횟수 검증
		verify(inquiryTemplateRepository, times(1)).findAllByKeyword(anyString(), any(Pageable.class));
	}

	@Test
	@DisplayName("문의 템플릿 미리보기 성공")
	void previewInquiryTemplate_success() {
		// given
		when(inquiryTemplateRepository.findById(anyLong())).thenReturn(java.util.Optional.of(inquiryTemplate));
		when(questionRepository.findAllByInquiryTemplate(any(InquiryTemplate.class))).thenReturn(questions);

		// when: 서비스 메서드 호출
		InquiryTemplatePreviewResDto result = inquiryTemplateService.previewInquiryTemplate(1L);

		// then: 결과 검증
		assertNotNull(result);
		assertEquals(reqDto.getName(), result.getName());
		assertEquals(reqDto.getDescription(), result.getDescription());
		assertEquals(reqDto.isActive(), result.isActive());
		assertEquals(reqDto.getQuestions().size(), result.getQuestions().size());

		// repository가 호출된 횟수 검증
		verify(inquiryTemplateRepository, times(1)).findById(anyLong());
		verify(questionRepository, times(1)).findAllByInquiryTemplate(any(InquiryTemplate.class));
	}

	@Test
	@DisplayName("문의 템플릿 미리보기 실패 - 템플릿 없음")
	void previewInquiryTemplate_notFound() {
		// given: 템플릿이 존재하지 않는 경우 설정
		when(inquiryTemplateRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

		// when & then: 예외 발생 검증
		ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
			inquiryTemplateService.previewInquiryTemplate(1L);
		});

		assertEquals("해당 문의 템플릿을 찾을 수 없습니다.", exception.getMessage());

		// repository가 호출된 횟수 검증
		verify(inquiryTemplateRepository, times(1)).findById(anyLong());
		verify(questionRepository, times(0)).findAllByInquiryTemplate(any(InquiryTemplate.class));
	}

	@Test
	@DisplayName("문의 템플릿 수정 성공")
	public void updateInquiryTemplate_success() {
		// given: 테스트 데이터 준비
		UpdateInquiryTemplateReqDto reqDto = UpdateInquiryTemplateReqDto.builder()
			.name("수정된 문의 템플릿")
			.description("수정된 템플릿입니다.")
			.isActive(false)
			.build();

		InquiryTemplate inquiryTemplate = InquiryTemplate.builder()
			.id(1L)
			.name(reqDto.getName())
			.description(reqDto.getDescription())
			.isActive(reqDto.getIsActive())
			.build();

		// Mock 객체의 행위 정의
		when(inquiryTemplateRepository.findById(anyLong())).thenReturn(java.util.Optional.of(inquiryTemplate));

		// when: 서비스 메서드 호출
		inquiryTemplateService.updateInquiryTemplate(1L, reqDto);

		// then: repository 메서드 호출 검증
		verify(inquiryTemplateRepository, times(1)).findById(anyLong());
		verify(inquiryTemplateRepository, times(1)).save(any(InquiryTemplate.class));

		ArgumentCaptor<InquiryTemplate> argumentCaptor = ArgumentCaptor.forClass(InquiryTemplate.class);
		// ArgumentCaptor를 사용하여 inquiryTemplateRepository.save() 메서드에 전달된 InquiryTemplate 객체 캡처
		verify(inquiryTemplateRepository, times(1)).save(argumentCaptor.capture());
		InquiryTemplate savedInquiryTemplate = argumentCaptor.getValue();

		// then: 응답 데이터 일치 여부 검증
		assertEquals(reqDto.getName(), savedInquiryTemplate.getName());
		assertEquals(reqDto.getDescription(), savedInquiryTemplate.getDescription());
		assertEquals(reqDto.getIsActive(), savedInquiryTemplate.getIsActive());
	}

	@Test
	@DisplayName("문의 템플릿 삭제 성공")
	void deleteInquiryTemplate_success() {
		// given: 템플릿이 존재하는 경우 설정
		when(inquiryTemplateRepository.findById(anyLong())).thenReturn(java.util.Optional.of(inquiryTemplate));

		// when: 서비스 메서드 호출
		inquiryTemplateService.deleteInquiryTemplate(1L);

		// then: repository 메서드 호출 검증
		verify(inquiryTemplateRepository, times(1)).findById(anyLong());
		verify(inquiryTemplateRepository, times(1)).delete(any(InquiryTemplate.class));

		// then: 삭제 후 템플릿 조회 검증
		when(inquiryTemplateRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());
		assertThrows(ResourceNotFoundException.class, () -> inquiryTemplateService.findInquiryTemplateById(1L));
	}

	@Test
	@DisplayName("문의 템플릿 삭제 실패 - 템플릿 없음")
	void deleteInquiryTemplate_fail_templateNotFound() {
		// given: 템플릿이 존재하지 않는 경우 설정
		when(inquiryTemplateRepository.findById(anyLong())).thenReturn(java.util.Optional.empty());

		// when, then: 예외 발생 검증
		assertThrows(ResourceNotFoundException.class, () -> inquiryTemplateService.deleteInquiryTemplate(1L));
	}
}