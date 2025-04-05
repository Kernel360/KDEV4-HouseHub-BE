package com.househub.backend.domain.inquiryTemplate.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;

import io.lettuce.core.dynamic.annotation.Param;

@Repository
public interface InquiryTemplateRepository extends JpaRepository<InquiryTemplate, Long> {

	// 🔄 findByIdAndRealEstateId
	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND it.id = :id " +
		"AND it.realEstate.id = :realEstateId")
	Optional<InquiryTemplate> findByIdAndRealEstateId(@Param("id") Long id, @Param("realEstateId") Long realEstateId);

	// 🔄 findAllByRealEstateIdAndFilters
	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND it.realEstate.id = :realEstateId " +
		"AND (:#{#active == null} = true OR it.active = :active) " +
		"AND (:#{#keyword == null or #keyword.isEmpty()} = true OR LOWER(it.name) LIKE %:keyword%)")
	Page<InquiryTemplate> findAllByRealEstateIdAndFilters(@Param("realEstateId") Long realEstateId,
		@Param("active") Boolean active,
		@Param("keyword") String keyword,
		Pageable pageable);

	// 🔄 findAllByRealEstateIdAndKeyword
	@Query("SELECT it FROM InquiryTemplate it " +
		"WHERE it.deletedAt IS NULL " +
		"AND it.realEstate.id = :realEstateId " +
		"AND (it.name LIKE %:keyword% OR it.description LIKE %:keyword%)")
	Page<InquiryTemplate> findAllByRealEstateIdAndKeyword(@Param("realEstateId") Long realEstateId,
		@Param("keyword") String keyword,
		Pageable pageable);

	// 🔄 existsByRealEstateIdAndName
	@Query("SELECT CASE WHEN COUNT(it) > 0 THEN true ELSE false END " +
		"FROM InquiryTemplate it " +
		"WHERE it.realEstate.id = :realEstateId " +
		"AND it.name = :name " +
		"AND it.deletedAt IS NULL")
	boolean existsByRealEstateIdAndName(@Param("realEstateId") Long realEstateId, @Param("name") String name);
}
