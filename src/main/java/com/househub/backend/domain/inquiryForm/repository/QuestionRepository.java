package com.househub.backend.domain.inquiryForm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.inquiryForm.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryForm.entity.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
	List<Question> findAllByInquiryTemplate(InquiryTemplate inquiryTemplate);
}
