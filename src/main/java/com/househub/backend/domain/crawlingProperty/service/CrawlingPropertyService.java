package com.househub.backend.domain.crawlingProperty.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyReqDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagListResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagResDto;

public interface CrawlingPropertyService {

	CrawlingPropertyTagResDto findOne(String id);

	CrawlingPropertyTagListResDto findAll(CrawlingPropertyReqDto crawlingPropertyReqDto, Pageable pageable);

	List<CrawlingPropertyResDto> findRecommendProperties(Long id, int limit, AgentResDto agentDto);
}