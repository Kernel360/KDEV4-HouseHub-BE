package com.househub.backend.domain.consultation.service;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.consultation.dto.ConsultationListResDto;
import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.dto.ConsultationResDto;
import com.househub.backend.domain.consultation.dto.UpdateConsultationReqDto;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;

public interface ConsultationService {

	ConsultationResDto create(ConsultationReqDto consultationReqDto, AgentResDto agent);

	ConsultationResDto findOne(Long id, Long agentId);

	ConsultationListResDto findAll(Long agentId, String keyword, LocalDate startDate, LocalDate endDate,
		ConsultationType type, ConsultationStatus status, Pageable pageable);

	ConsultationListResDto findAllByCustomer(Long customerId, Long agentId,
		Pageable pageable);

	ConsultationResDto update(Long id, UpdateConsultationReqDto consultationReqDto, AgentResDto agentDto);

	ConsultationResDto delete(Long id, Long agentId);
}
