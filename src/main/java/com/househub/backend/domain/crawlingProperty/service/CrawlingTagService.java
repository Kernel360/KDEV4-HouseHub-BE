package com.househub.backend.domain.crawlingProperty.service;

import com.househub.backend.domain.crawlingProperty.dto.CrawlingTagResDto;

import java.util.List;

public interface CrawlingTagService {

    List<CrawlingTagResDto> findAll();
}
