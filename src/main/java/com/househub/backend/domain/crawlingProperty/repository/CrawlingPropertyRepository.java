package com.househub.backend.domain.crawlingProperty.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;

public interface CrawlingPropertyRepository
	extends JpaRepository<CrawlingProperty, String>, CrawlingPropertyRepositoryCustom {

	// findOne
	Optional<CrawlingProperty> findById(String id);

	@Query(value = """
		SELECT cp.*, sub.match_count 
		FROM (
		    SELECT cptm.crawling_properties_id, COUNT(*) AS match_count
		    FROM crawling_property_tag_map cptm
		    WHERE cptm.tag_id IN (:tagIds)
		    GROUP BY cptm.crawling_properties_id
		    ORDER BY match_count DESC
		    LIMIT :limit
		) AS sub
		JOIN crawling_properties cp ON cp.crawling_properties_id = sub.crawling_properties_id
		ORDER BY sub.match_count DESC
		""", nativeQuery = true)
	List<CrawlingProperty> findTopNByMatchingTags(
		@Param("tagIds") List<Long> tagIds,
		@Param("limit") int limit,
		@Param("agentId") Long agentId
	);
}