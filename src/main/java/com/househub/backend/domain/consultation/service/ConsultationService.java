package com.househub.backend.domain.consultation.service;

import com.househub.backend.domain.consultation.dto.ConsultationReqDto;
import com.househub.backend.domain.consultation.dto.ConsultationResDto;
import com.househub.backend.domain.consultation.entity.Consultation;

import java.util.List;

public interface ConsultationService {

    ConsultationResDto create(ConsultationReqDto consultationReqDto);

    ConsultationResDto findOne(Long id);

    List<ConsultationResDto> findAll();

    ConsultationResDto update(Long id, ConsultationReqDto consultationReqDto);

    ConsultationResDto delete(Long id);
}
