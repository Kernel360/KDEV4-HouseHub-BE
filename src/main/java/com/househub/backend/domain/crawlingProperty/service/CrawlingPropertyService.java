package com.househub.backend.domain.crawlingProperty.service;

import com.househub.backend.domain.crawlingProperty.dto.*;
import com.househub.backend.domain.crawlingProperty.entity.CrawlingProperty;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrawlingPropertyService {

    CrawlingPropertyTagResDto findOne(String id);

    CrawlingPropertyTagListResDto findAll(CrawlingPropertyReqDto crawlingPropertyReqDto, Pageable pageable);
}