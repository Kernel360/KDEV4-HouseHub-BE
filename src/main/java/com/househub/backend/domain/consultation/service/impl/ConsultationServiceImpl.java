package com.househub.backend.domain.consultation.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.househub.backend.common.exception.BusinessException;
import com.househub.backend.common.exception.ErrorCode;
import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.repository.AgentRepository;
import com.househub.backend.domain.consultation.dto.ConsultationListResDto;
import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.dto.ConsultationResDto;
import com.househub.backend.domain.consultation.dto.UpdateConsultationReqDto;
import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import com.househub.backend.domain.consultation.repository.ConsultationRepository;
import com.househub.backend.domain.consultation.service.ConsultationService;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.repository.CustomerRepository;

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

	@Transactional
	public ConsultationResDto create(
		ConsultationReqDto consultationReqDto,
		AgentResDto agentDto
	) {
		Agent agent = agentDto.toEntity();
		Long customerId = consultationReqDto.getCustomerId();
		Customer customer = null;
		Customer newCustomer = null;

		// customerId가 null이면 신규 고객 처리
		if (customerId == null && consultationReqDto.getNewCustomer() != null) {
			ConsultationReqDto.NewCustomerDto newCustomerDto = consultationReqDto.getNewCustomer();
			// contact 기반으로 기존 고객 찾음 (신규 고객의 연락처로)
			customer = customerRepository.findByContactAndAgentIdAndDeletedAtIsNull(
				newCustomerDto.getContact(), agent.getId()
			).orElse(null);
			log.info("{}", customer.getName());

			if (customer != null) {
				throw new BusinessException(ErrorCode.CONFLICT_CUSTOMER_CONTACT);
			}

			// 고객이 없으면 새로 생성
			if (customer == null) {
				newCustomer = customerRepository.save(newCustomerDto.toEntity(agent));
				customer = newCustomer; // 새로 생성된 고객을 연결
			}
		} else if (customerId != null) {
			// customerId가 있으면 기존 고객 조회
			customer = customerRepository.findByIdAndAgentIdAndDeletedAtIsNull(customerId, agent.getId())
				.orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND));
		} else if (customerId == null && consultationReqDto.getNewCustomer() == null) {
			throw new BusinessException(ErrorCode.MISSING_CUSTOMER_INFORMATION);
		}

		// 상담 등록
		Consultation consultation = consultationReqDto.toEntity(agent, customer, newCustomer);

		// 상담 저장 후 반환
		return ConsultationResDto.fromEntity(consultationRepository.save(consultation));
	}

	@Transactional
	public ConsultationResDto findOne(Long id, Long agentId) {
		Agent agent = validateAgent(agentId);

		Consultation consultation = consultationRepository.findByIdAndAgentAndDeletedAtIsNull(id, agent)
			.orElseThrow(() -> new ResourceNotFoundException("해당 상담 내역이 존재하지 않습니다:" + id, "CONSULTATION_NOT_FOUND"));
		return ConsultationResDto.fromEntity(consultation);
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
