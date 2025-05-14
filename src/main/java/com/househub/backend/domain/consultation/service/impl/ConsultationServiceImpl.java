package com.househub.backend.domain.consultation.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.consultation.dto.ConsultationDetailResDto;
import com.househub.backend.domain.consultation.dto.ConsultationListResDto;
import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.dto.ConsultationResDto;
import com.househub.backend.domain.consultation.dto.UpdateConsultationReqDto;
import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.entity.ConsultationProperty;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import com.househub.backend.domain.consultation.repository.ConsultationRepository;
import com.househub.backend.domain.consultation.service.ConsultationService;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;
import com.househub.backend.domain.property.entity.Property;
import com.househub.backend.domain.property.service.PropertyReader;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsultationServiceImpl implements ConsultationService {

	private final ConsultationRepository consultationRepository;
	private final CustomerRepository customerRepository;
	private final AgentRepository agentRepository;
	private final PropertyReader propertyReader;

	@Transactional
	public ConsultationResDto create(
		ConsultationReqDto consultationReqDto,
		AgentResDto agentDto
	) {
		Agent agent = agentDto.toEntity();
		Long customerId = consultationReqDto.getCustomerId();
		Customer customer;

		if (customerId != null) {
			customer = customerRepository.findByIdAndAgentIdAndDeletedAtIsNull(customerId, agent.getId())
				.orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
		} else if (consultationReqDto.getNewCustomer() != null) {
			ConsultationReqDto.NewCustomerDto newCustomerDto = consultationReqDto.getNewCustomer();
			customer = customerRepository.findByContactAndAgentIdAndDeletedAtIsNull(newCustomerDto.getContact(),
					agent.getId())
				.orElse(null);
			// 신규 고객 정보가 존재하지 않으면 신규 고객 생성
			if (customer == null) {
				customer = customerRepository.save(newCustomerDto.toEntity(agent));
			}
		} else {
			throw new BusinessException(ErrorCode.MISSING_CUSTOMER_INFORMATION);
		}

		// 상담 등록
		Consultation consultation = consultationReqDto.toEntity(agent, customer);

		// 선택된 매물 ID 리스트 처리
		List<Long> selectedPropertyIds = Optional.ofNullable(consultationReqDto.getShownPropertyIds())
			.orElse(Collections.emptyList())
			.stream()
			.distinct()
			.toList();

		if (!selectedPropertyIds.isEmpty()) {
			// agent 소유 매물만 조회
			List<Property> properties = propertyReader.findAgentOwnedPropertiesByIds(
				selectedPropertyIds,
				agent.getId()
			);

			// 실제로 등록된 매물 ID들
			Set<Long> foundPropertyIds = properties.stream()
				.map(Property::getId)
				.collect(Collectors.toSet());

			// 누락된 매물 ID들
			List<Long> missingPropertyIds = selectedPropertyIds.stream()
				.filter(id -> !foundPropertyIds.contains(id))
				.toList();

			// 등록 가능한 매물들만 등록
			List<ConsultationProperty> consultationProperties = properties.stream()
				.map(property -> ConsultationProperty.builder()
					.consultation(consultation)
					.property(property)
					.build())
				.toList();

			consultation.getConsultationProperties().addAll(consultationProperties);

			// 누락된 매물이 있을 경우 로그 기록
			if (!missingPropertyIds.isEmpty()) {
				log.info("Missing properties ({} of {}) for agent {}: {}",
					missingPropertyIds.size(),
					selectedPropertyIds.size(),
					agent.getId(),
					missingPropertyIds);
			}
		}

		// 상담 저장 후 반환
		Consultation savedConsultation = consultationRepository.save(consultation);
		return ConsultationResDto.fromEntity(savedConsultation);

	}

	@Transactional
	public ConsultationDetailResDto findOne(Long id, Long agentId) {
		Agent agent = validateAgent(agentId);

		Consultation consultation = consultationRepository.findWithPropertiesByIdAndAgentAndDeletedAtIsNull(id, agent)
			.orElseThrow(() -> new ResourceNotFoundException("해당 상담 내역이 존재하지 않습니다:" + id, "CONSULTATION_NOT_FOUND"));
		return ConsultationDetailResDto.fromEntity(consultation);
	}

	@Transactional
	@Override
	public ConsultationListResDto findAll(
		Long agentId,
		String keyword,
		LocalDate startDate,
		LocalDate endDate,
		ConsultationType type,
		ConsultationStatus status,
		Pageable pageable
	) {
		Agent agent = validateAgent(agentId);

		LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;
		LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

		Page<ConsultationResDto> consultations = consultationRepository.searchConsultations(
			agentId,
			keyword,
			startDateTime,
			endDateTime,
			type,
			status,
			pageable
		).map(ConsultationResDto::fromEntity);

		return ConsultationListResDto.fromPage(consultations);
	}

	@Transactional
	@Override
	public ConsultationListResDto findAllByCustomer(
		Long customerId,
		Long agentId,
		Pageable pageable
	) {
		Agent agent = validateAgent(agentId);
		Page<ConsultationResDto> consultations = consultationRepository.searchConsultationsByCustomerName(
			agentId,
			customerId,
			pageable
		).map(ConsultationResDto::fromEntity);

		return ConsultationListResDto.fromPage(consultations);
	}

	@Transactional
	public ConsultationResDto update(
		Long id,
		UpdateConsultationReqDto consultationReqDto,
		AgentResDto agentDto
	) {
		Agent agent = agentDto.toEntity();

		Consultation consultation = consultationRepository.findByIdAndAgentAndDeletedAtIsNull(id, agent)
			.orElseThrow(() -> new ResourceNotFoundException("해당 상담 내역이 존재하지 않습니다:" + id, "CONSULTATION_NOT_FOUND"));

		consultation.update(consultationReqDto);

		if (consultationReqDto.getShownPropertyIds() != null) {
			// 기존 매물 관계 제거
			consultation.clearShownProperties();

			// 신규 매물 추가
			List<Property> properties = propertyReader.findAgentOwnedPropertiesByIds(
				consultationReqDto.getShownPropertyIds(),
				agent.getId()
			);
			properties.forEach(consultation::addShownProperty);
		}

		return ConsultationResDto.fromEntity(consultation);
	}

	@Transactional
	public ConsultationResDto delete(Long id, Long agentId) {
		Agent agent = validateAgent(agentId);

		Consultation consultation = consultationRepository.findByIdAndAgentAndDeletedAtIsNull(id, agent)
			.orElseThrow(() -> new ResourceNotFoundException("해당 상담 내역이 존재하지 않습니다:" + id, "CONSULTATION_NOT_FOUND"));

		consultation.softDelete();

		return ConsultationResDto.fromEntity(consultation);
	}

	private Agent validateAgent(Long agentId) {
		return agentRepository.findById(agentId)
			.orElseThrow(() -> new ResourceNotFoundException("공인중개사가 존재하지 않습니다.", "AGENT_NOT_FOUND"));
	}
}
