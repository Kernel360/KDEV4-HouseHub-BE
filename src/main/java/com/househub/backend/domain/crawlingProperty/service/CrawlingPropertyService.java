package com.househub.backend.domain.crawlingProperty.service;

import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyReqDto;
import com.househub.backend.domain.crawlingProperty.dto.CrawlingPropertyResDto;

import java.util.List;

public interface CrawlingPropertyService {

    CrawlingPropertyResDto findOne(String id);

    List<CrawlingPropertyResDto> findAll(CrawlingPropertyReqDto crawlingPropertyReqDto);
}
