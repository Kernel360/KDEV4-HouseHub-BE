package com.househub.backend.domain.inquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.inquiry.entity.InquiryAnswer;

@Repository
public interface InquiryAnswerRepository extends JpaRepository<InquiryAnswer, Long> {
}
