package com.househub.backend.domain.consultation.service;

import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.dto.ConsultationResDto;

import java.util.List;

public interface ConsultationService {

    ConsultationResDto create(ConsultationReqDto consultationReqDto, Long agentId);

    ConsultationResDto findOne(Long id, Long agentId);

    List<ConsultationResDto> findAll(Long agentId);

    ConsultationResDto update(Long id, ConsultationReqDto consultationReqDto, Long agentId);

    ConsultationResDto delete(Long id, Long agentId);
}
