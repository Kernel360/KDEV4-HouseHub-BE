package com.househub.backend.domain.crawlingProperty.service.impl;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.crawlingProperty.dto.*;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingPropertyTagMap;
import com.househub.backend.domain.crawlingProperty.entity.Tag;
import com.househub.backend.domain.crawlingProperty.repository.CrawlingPropertyRepository;
import com.househub.backend.domain.crawlingProperty.service.CrawlingPropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingPropertyServiceImpl implements CrawlingPropertyService {

    private final CrawlingPropertyRepository crawlingPropertyRepository;

    // 조건만 검색하는 경우
    @Transactional(readOnly = true)
    @Override
    public CrawlingPropertyTagListResDto findAll(
        CrawlingPropertyReqDto crawlingPropertyReqDto,
        Pageable pageable
    ) {
        Page<CrawlingProperty> crawlingPropertyList = crawlingPropertyRepository.findAllWithTags(
                crawlingPropertyReqDto.getTransactionType(),
                crawlingPropertyReqDto.getPropertyType(),
                crawlingPropertyReqDto.getProvince(),
                crawlingPropertyReqDto.getCity(),
                crawlingPropertyReqDto.getDong(),
                correctMinValue(crawlingPropertyReqDto.getMinSalePrice()), // 최소값 0 적용
                crawlingPropertyReqDto.getMaxSalePrice(),
                correctMinValue(crawlingPropertyReqDto.getMinDeposit()),
                crawlingPropertyReqDto.getMaxDeposit(),
                correctMinValue(crawlingPropertyReqDto.getMinMonthlyRent()),
                crawlingPropertyReqDto.getMaxMonthlyRent(),
                pageable
        );

        if (crawlingPropertyList.isEmpty()) {
            throw new ResourceNotFoundException("크롤링 매물 목록이 존재하지 않습니다:", "CRAWLING_PROPERTIES_NOT_FOUND");
        }

        // 1. 태그들을 매물 ID 기준으로 묶기
        Map<String, List<Tag>> tagMap = crawlingPropertyList.getContent().stream()
                .flatMap(cp -> cp.getCrawlingPropertyTagMaps().stream()
                        .map(cptm -> Map.entry(cp.getCrawlingPropertiesId(), cptm.getTag())))
                .collect(Collectors.groupingBy(
                        Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                ));

        // 2. CrawlingProperty → CrawlingPropertyTagResDto 변환
        List<CrawlingPropertyTagResDto> dtoList = crawlingPropertyList.getContent().stream()
                .map(cp -> CrawlingPropertyTagResDto.fromEntity(cp, tagMap))
                .toList();

        Page<CrawlingPropertyTagResDto> response = new PageImpl<>(dtoList, pageable, crawlingPropertyList.getTotalElements());

        return CrawlingPropertyTagListResDto.fromPage(response);
    }

    // 조건 및 태그 검색하는 경우
    @Transactional(readOnly = true)
    @Override
    public CrawlingPropertyTagListResDto findPropertiesByTags(
            List<Long> tagIds,
            CrawlingPropertyReqDto crawlingPropertyReqDto,
            Pageable pageable
    ) {
        // 1차 필터
        List<CrawlingProperty> crawlingPropertyList = crawlingPropertyRepository.findByDto(
                crawlingPropertyReqDto.getTransactionType(),
                crawlingPropertyReqDto.getPropertyType(),
                crawlingPropertyReqDto.getProvince(),
                crawlingPropertyReqDto.getCity(),
                crawlingPropertyReqDto.getDong(),
                correctMinValue(crawlingPropertyReqDto.getMinSalePrice()), // 최소값 0 적용
                crawlingPropertyReqDto.getMaxSalePrice(),
                correctMinValue(crawlingPropertyReqDto.getMinDeposit()),
                crawlingPropertyReqDto.getMaxDeposit(),
                correctMinValue(crawlingPropertyReqDto.getMinMonthlyRent()),
                crawlingPropertyReqDto.getMaxMonthlyRent()
        );

        if (crawlingPropertyList.isEmpty()) {
            throw new ResourceNotFoundException("크롤링 매물 목록이 존재하지 않습니다:", "CRAWLING_PROPERTIES_NOT_FOUND");
        }

        // id 리스트 변환
        List<String> propertyIds = crawlingPropertyList.stream()
                .map(CrawlingProperty::getCrawlingPropertiesId)
                .toList();

        Page<CrawlingPropertyTagResDto> response = crawlingPropertyRepository.findByTags(propertyIds, tagIds, pageable);

        return CrawlingPropertyTagListResDto.fromPage(response);
    }

    // 최소 가격을 0으로 설정
    private Float correctMinValue(Float value) {
        if (value == null) return null;
        return Math.max(0.0f, value);
    }

    // 상세 조회
    public CrawlingPropertyResDto findOne(String id) {
        CrawlingProperty crawlingProperty = crawlingPropertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 매물이 존재하지 않습니다.", "PROPERTY_NOT_FOUND"));

        return CrawlingPropertyResDto.fromEntity(crawlingProperty);
    }
}
