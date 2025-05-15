package com.househub.backend.domain.crawlingProperty.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.agent.dto.AgentResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyReqDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagListResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyTagResDto;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import com.househub.backend.domain.crawlingProperty.repository.CrawlingPropertyRepository;
import com.househub.backend.domain.crawlingProperty.service.CrawlingPropertyService;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerReader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingPropertyServiceImpl implements CrawlingPropertyService {

	private final CrawlingPropertyRepository crawlingPropertyRepository;
	private final CustomerReader customerReader;

	// 조건만 검색하는 경우
	@Transactional(readOnly = true)
	@Override
	public CrawlingPropertyTagListResDto findAll(
		CrawlingPropertyReqDto crawlingPropertyReqDto,
		Pageable pageable
	) {
		log.info("크롤링 매물 목록 조회 요청 - crawlingPropertyReqDto={}", crawlingPropertyReqDto.toString());

		Page<CrawlingProperty> crawlingPropertyPage = crawlingPropertyRepository.findAllWithTags(
			crawlingPropertyReqDto.getTransactionType(),
			crawlingPropertyReqDto.getPropertyType(),
			crawlingPropertyReqDto.getProvince(),
			crawlingPropertyReqDto.getCity(),
			crawlingPropertyReqDto.getDong(),
			crawlingPropertyReqDto.getMinSalePrice(),
			crawlingPropertyReqDto.getMaxSalePrice(),
			crawlingPropertyReqDto.getMinDeposit(),
			crawlingPropertyReqDto.getMaxDeposit(),
			crawlingPropertyReqDto.getMinMonthlyRent(),
			crawlingPropertyReqDto.getMaxMonthlyRent(),
			crawlingPropertyReqDto.getTagIds(),
			pageable
		);
		Page<CrawlingPropertyTagResDto> crawlingPropertyTagResDtoPage = crawlingPropertyPage.map(
			CrawlingPropertyTagResDto::fromEntity
		);

		return CrawlingPropertyTagListResDto.fromPage(crawlingPropertyTagResDtoPage);
	}

	@Override
	public List<CrawlingPropertyResDto> findRecommendProperties(Long id, int limit, AgentResDto agentDto) {
		Customer customer = customerReader.findByIdAndDeletedAtIsNotNullOrThrow(id, agentDto.getId());
		List<Long> tagIds = customer.getCustomerTagMaps().stream().map(map -> map.getTag().getTagId()).toList();
		List<CrawlingProperty> crawlingPropertyList = crawlingPropertyRepository.findTopNByMatchingTags(tagIds, limit,
			agentDto.getId());

		return crawlingPropertyList.stream().map(CrawlingPropertyResDto::fromEntity).toList();
	}

	// 최소 가격을 0으로 설정
	private Float correctMinValue(Float value) {
		if (value == null)
			return null;
		return Math.max(0.0f, value);
	}

	// 상세 조회
	public CrawlingPropertyTagResDto findOne(String id) {
		CrawlingPropertyTagResDto crawlingPropertyTagResDto = crawlingPropertyRepository.findCrawlingPropertyById(id)
			.orElseThrow(() -> new ResourceNotFoundException("해당 매물이 존재하지 않습니다.", "PROPERTY_NOT_FOUND"));

		return crawlingPropertyTagResDto;
	}
}