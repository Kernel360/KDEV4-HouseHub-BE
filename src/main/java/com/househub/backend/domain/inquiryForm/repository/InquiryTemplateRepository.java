package com.househub.backend.domain.inquiryForm.repository;


import com.househub.backend.domain.inquiryForm.entity.InquiryTemplate;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryTemplateRepository extends JpaRepository<InquiryTemplate, Long> {
    @Query("SELECT it FROM InquiryTemplate it " +
        "WHERE it.deletedAt IS NULL " +
        "AND (:isActive IS NULL OR it.isActive = :isActive)")
    Page<InquiryTemplate> findAllByFilters(@Param("isActive") Boolean isActive, Pageable pageable);
}
