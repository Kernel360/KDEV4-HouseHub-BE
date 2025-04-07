package com.househub.backend.domain.inquiry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.inquiry.entity.Inquiry;

@Repository
public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
