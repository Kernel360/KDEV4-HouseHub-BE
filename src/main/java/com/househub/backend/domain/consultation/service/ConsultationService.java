package com.househub.backend.domain.consultation.service;

import java.time.LocalDate;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.consultation.dto.ConsultationListResDto;
import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.dto.ConsultationResDto;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;

public interface ConsultationService {

	ConsultationResDto create(ConsultationReqDto consultationReqDto, Long agentId);

	ConsultationResDto findOne(Long id, Long agentId);

	ConsultationListResDto findAll(Long agentId, String keyword, LocalDate startDate, LocalDate endDate,
		ConsultationType type, ConsultationStatus status, Pageable pageable);

	ConsultationListResDto findAllByCustomer(Long agentId,
		String customerName,
		Pageable pageable);

	ConsultationResDto update(Long id, ConsultationReqDto consultationReqDto, Long agentId);

	ConsultationResDto delete(Long id, Long agentId);
}
