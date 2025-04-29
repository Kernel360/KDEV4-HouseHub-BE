package com.househub.backend.domain.crawlingProperty.repository;

import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagResDto;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrawlingPropertyRepositoryCustom {

    Page<CrawlingPropertyTagResDto> findByTags(List<String> propertyIds, List<Long> tagIds, Pageable pageable);
}
