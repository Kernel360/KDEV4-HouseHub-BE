package com.househub.backend.domain.inquiryTemplate.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	@Query("SELECT q FROM Question q WHERE q.inquiryTemplate.id = :templateId and q.inquiryTemplate.deletedAt is null")
	List<Question> findAllByInquiryTemplateId(Long templateId);

	List<Question> findAllByInquiryTemplate(InquiryTemplate inquiryTemplate);

	@Query("SELECT DISTINCT q FROM Question q " +
		"LEFT JOIN FETCH q.options o " +
		"WHERE q.inquiryTemplate = :inquiryTemplate " +
		"ORDER BY q.questionOrder")
	List<Question> findAllByInquiryTemplateWithOptions(@Param("inquiryTemplate") InquiryTemplate inquiryTemplate);
}
