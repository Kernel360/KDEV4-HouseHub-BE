package com.househub.backend.domain.inquiryTemplate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.inquiryTemplate.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	@Query("SELECT q FROM Question q WHERE q.inquiryTemplate.id = :templateId and q.inquiryTemplate.deletedAt is null")
	List<Question> findAllByInquiryTemplateId(Long templateId);
}
