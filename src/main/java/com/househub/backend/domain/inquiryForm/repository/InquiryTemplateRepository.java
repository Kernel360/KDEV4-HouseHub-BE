package com.househub.backend.domain.inquiryForm.repository;


import com.househub.backend.domain.inquiryForm.entity.InquiryTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryTemplateRepository extends JpaRepository<InquiryTemplate, Long> {
}
