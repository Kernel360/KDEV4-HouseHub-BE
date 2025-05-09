package com.househub.backend.domain.crawlingProperty.service;

import com.househub.backend.domain.crawlingProperty.dto.*;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyListResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyReqDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagListResDto;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrawlingPropertyService {

    CrawlingPropertyTagResDto findOne(String id);

    CrawlingPropertyTagListResDto findAll(CrawlingPropertyReqDto crawlingPropertyReqDto, Pageable pageable);

	List<CrawlingPropertyResDto> findRecommendProperties(Long id, int limit, AgentResDto agentDto);
}