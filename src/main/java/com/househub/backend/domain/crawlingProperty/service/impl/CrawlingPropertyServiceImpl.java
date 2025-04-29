package com.househub.backend.domain.crawlingProperty.service.impl;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyListResDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyReqDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingPropertyTagMap;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingPropertyServiceImpl implements CrawlingPropertyService {

    private final CrawlingPropertyRepository crawlingPropertyRepository;

    @Transactional(readOnly = true)
    @Override
    public CrawlingPropertyListResDto findAll(
        CrawlingPropertyReqDto crawlingPropertyReqDto,
        Pageable pageable
    ) {
        Page<CrawlingProperty> crawlingPropertyList = crawlingPropertyRepository.findByDto(
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

        Page<CrawlingPropertyResDto> response = crawlingPropertyList.map(CrawlingPropertyResDto::fromEntity);

        return CrawlingPropertyListResDto.fromPage(response);
    }

    @Transactional(readOnly = true)
    @Override
    public CrawlingPropertyListResDto findPropertiesByTags(
            List<Long> tagIds,
            CrawlingPropertyReqDto crawlingPropertyReqDto,
            Pageable pageable
    ) {
        // 1차 필터
        Page<CrawlingProperty> crawlingPropertyList = crawlingPropertyRepository.findByDto(
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

        // crawlingPropertiesId 리스트만 추출
        List<CrawlingProperty> searchedCrawlingProperty = crawlingPropertyList.getContent().stream()
                .filter(cp -> {
                    List<Long> tagMapList = cp.getCrawlingPropertyTagMaps().stream()
                            .map(cptm -> cptm.getTag().getTagId()) // 매핑을 tagId로 바꿔
                            .toList();

                    return tagIds.stream().anyMatch(tagMapList::contains);
                })
                .toList();

        // Page 객체로 변환
        Page<CrawlingProperty> searchedCrawlingPropertyPage = new PageImpl<>(
                searchedCrawlingProperty,
                crawlingPropertyList.getPageable(), // 기존 pageable 그대로 재사용
                crawlingPropertyList.getTotalElements() // 전체 데이터 개수 그대로
        );

        Page<CrawlingPropertyResDto> response = searchedCrawlingPropertyPage.map(CrawlingPropertyResDto::fromEntity);

        return CrawlingPropertyListResDto.fromPage(response);
    }


//    @Transactional(readOnly = true)
//    @Override
//    public CrawlingPropertyListResDto findPropertiesByTags2(
//            List<Long> tagIds,
//            CrawlingPropertyReqDto crawlingPropertyReqDto,
//            Pageable pageable
//    ) {
//        // 1차 필터
//        List<CrawlingProperty> crawlingPropertyList = crawlingPropertyRepository.findByDto2(
//                crawlingPropertyReqDto.getTransactionType(),
//                crawlingPropertyReqDto.getPropertyType(),
//                crawlingPropertyReqDto.getProvince(),
//                crawlingPropertyReqDto.getCity(),
//                crawlingPropertyReqDto.getDong(),
//                correctMinValue(crawlingPropertyReqDto.getMinSalePrice()), // 최소값 0 적용
//                crawlingPropertyReqDto.getMaxSalePrice(),
//                correctMinValue(crawlingPropertyReqDto.getMinDeposit()),
//                crawlingPropertyReqDto.getMaxDeposit(),
//                correctMinValue(crawlingPropertyReqDto.getMinMonthlyRent()),
//                crawlingPropertyReqDto.getMaxMonthlyRent()
//        );
//
//        if (crawlingPropertyList.isEmpty()) {
//            throw new ResourceNotFoundException("크롤링 매물 목록이 존재하지 않습니다:", "CRAWLING_PROPERTIES_NOT_FOUND");
//        }
//
//        // id 리스트 변환
//        List<String> propertyIds = crawlingPropertyList.stream()
//                .map(CrawlingProperty::getCrawlingPropertiesId)
//                .toList();
//
//        // 2차 태그 매칭 + 페이징 정렬
//        Page<CrawlingPropertyResDto> response = crawlingPropertyRepository.findByAnyMatchingTags2(propertyIds, tagIds, pageable);
//
//        return CrawlingPropertyListResDto.fromPage(response);
//    }



    // 최소 가격을 0으로 설정
    private Float correctMinValue(Float value) {
        if (value == null) return null;
        return Math.max(0.0f, value);
    }

    public CrawlingPropertyResDto findOne(String id) {
        CrawlingProperty crawlingProperty = crawlingPropertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 매물이 존재하지 않습니다.", "PROPERTY_NOT_FOUND"));

        return CrawlingPropertyResDto.fromEntity(crawlingProperty);
    }
}
