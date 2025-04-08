package com.househub.backend.domain.crawlingProperty.service.impl;

import com.househub.backend.common.exception.ResourceNotFoundException;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyReqDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import com.househub.backend.domain.crawlingProperty.repository.CrawlingPropertyRepository;
import com.househub.backend.domain.crawlingProperty.service.CrawlingPropertyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingPropertyServiceImpl implements CrawlingPropertyService {

    private final CrawlingPropertyRepository crawlingPropertyRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CrawlingPropertyResDto> findAll(
        CrawlingPropertyReqDto crawlingPropertyReqDto,
        Pageable pageable
    ) {
        Page<CrawlingProperty> crawlingPropertyList = crawlingPropertyRepository.findByDto(
                crawlingPropertyReqDto.getPropertyType(),
                crawlingPropertyReqDto.getTransactionType(),
                crawlingPropertyReqDto.getProvince(),
                crawlingPropertyReqDto.getCity(),
                crawlingPropertyReqDto.getDong(),
                crawlingPropertyReqDto.getDetailAddress(),
                crawlingPropertyReqDto.getMinArea(),
                crawlingPropertyReqDto.getMaxArea(),
                crawlingPropertyReqDto.getMinSalePrice(),
                crawlingPropertyReqDto.getMaxSalePrice(),
                crawlingPropertyReqDto.getMinDeposit(),
                crawlingPropertyReqDto.getMaxDeposit(),
                crawlingPropertyReqDto.getMinMonthlyRent(),
                crawlingPropertyReqDto.getMaxMonthlyRent(),
                pageable
        );

        System.out.println("=== CrawlingPropertyReqDto ===");
        System.out.println("propertyType: " + crawlingPropertyReqDto.getPropertyType());
        System.out.println("transactionType: " + crawlingPropertyReqDto.getTransactionType());
        System.out.println("province: " + crawlingPropertyReqDto.getProvince());
        System.out.println("city: " + crawlingPropertyReqDto.getCity());
        System.out.println("dong: " + crawlingPropertyReqDto.getDong());
        System.out.println("detailAddress: " + crawlingPropertyReqDto.getDetailAddress());
        System.out.println("minArea: " + crawlingPropertyReqDto.getMinArea());
        System.out.println("maxArea: " + crawlingPropertyReqDto.getMaxArea());
        System.out.println("minSalePrice: " + crawlingPropertyReqDto.getMinSalePrice());
        System.out.println("maxSalePrice: " + crawlingPropertyReqDto.getMaxSalePrice());
        System.out.println("minDeposit: " + crawlingPropertyReqDto.getMinDeposit());
        System.out.println("maxDeposit: " + crawlingPropertyReqDto.getMaxDeposit());
        System.out.println("minMonthlyRent: " + crawlingPropertyReqDto.getMinMonthlyRent());
        System.out.println("maxMonthlyRent: " + crawlingPropertyReqDto.getMaxMonthlyRent());
        System.out.println("===============================");

        if (crawlingPropertyList.isEmpty()) {
            throw new ResourceNotFoundException("크롤링 매물 목록이 존재하지 않습니다:", "CRAWLING_PROPERTIES_NOT_FOUND");
        }

        return crawlingPropertyList.stream()
                .map(CrawlingPropertyResDto::fromEntity)
                .toList();
    }

    public CrawlingPropertyResDto findOne(String id) {
        CrawlingProperty crawlingProperty = crawlingPropertyRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("해당 매물이 존재하지 않습니다.", "PROPERTY_NOT_FOUND"));

        return CrawlingPropertyResDto.fromEntity(crawlingProperty);
    }
}
