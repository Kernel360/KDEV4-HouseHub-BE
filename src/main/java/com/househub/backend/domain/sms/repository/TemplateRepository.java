package com.househub.backend.domain.sms.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.househub.backend.domain.agent.entity.RealEstate;
import com.househub.backend.domain.sms.entity.SmsTemplate;

public interface TemplateRepository extends JpaRepository<SmsTemplate, Long> {

	List<SmsTemplate> findAllByRealEstateAndDeletedAtIsNull(RealEstate realEstate);

	Optional<SmsTemplate> findByIdAndRealEstateAndDeletedAtIsNull(Long id, RealEstate realEstate);
}
