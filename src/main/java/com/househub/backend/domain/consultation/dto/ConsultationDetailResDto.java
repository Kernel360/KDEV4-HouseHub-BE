package com.househub.backend.domain.consultation.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.househub.backend.domain.consultation.entity.Consultation;
import com.househub.backend.domain.consultation.enums.ConsultationStatus;
import com.househub.backend.domain.consultation.enums.ConsultationType;
import com.househub.backend.domain.property.dto.PropertySummaryResDto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConsultationDetailResDto {

	private Long id;
	private Long agentId;
	private CustomerSummaryInConsultationDto customer;

	private ConsultationType consultationType; // PHONE, VISIT
	private String content;
	private LocalDateTime consultationDate;
	private ConsultationStatus status; // RESERVED, COMPLETED, CANCELED

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private LocalDateTime deletedAt;
	private List<PropertySummaryResDto> shownProperties; // 상담 시 보여준 매물 목록

	public static ConsultationDetailResDto fromEntity(Consultation consultation) {
		return ConsultationDetailResDto.builder()
			.id(consultation.getId())
			.agentId(consultation.getAgent().getId())
			.customer(CustomerSummaryInConsultationDto.fromEntity(consultation.getCustomer()))
			.consultationType(consultation.getConsultationType())
			.content(consultation.getContent())
			.consultationDate(consultation.getConsultationDate())
			.status(consultation.getStatus())
			.createdAt(consultation.getCreatedAt())
			.updatedAt(consultation.getUpdatedAt())
			.deletedAt(consultation.getDeletedAt())
			.shownProperties(
				consultation.getConsultationProperties().stream()
					.map(cp -> PropertySummaryResDto.fromEntity(cp.getProperty()))
					.toList()
			)
			.build();
	}
}
