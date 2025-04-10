package com.househub.backend.domain.inquiryTemplate.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplateSharedToken;

@Repository
public interface InquiryTemplateSharedTokenRepository extends JpaRepository<InquiryTemplateSharedToken, Long> {
	Optional<InquiryTemplateSharedToken> findByShareTokenAndActiveTrue(String shareToken);

	Optional<InquiryTemplateSharedToken> findTopByTemplateAndActiveIsTrueOrderByCreatedAtDesc(InquiryTemplate template);

}
