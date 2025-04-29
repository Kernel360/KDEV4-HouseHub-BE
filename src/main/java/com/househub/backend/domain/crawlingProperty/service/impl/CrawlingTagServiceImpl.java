package com.househub.backend.domain.crawlingProperty.service.impl;

import com.househub.backend.domain.crawlingProperty.dto.CrawlingTagResDto;
import com.househub.backend.domain.crawlingProperty.entity.Tag;
import com.househub.backend.domain.crawlingProperty.repository.TagRepository;
import com.househub.backend.domain.crawlingProperty.service.CrawlingTagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrawlingTagServiceImpl implements CrawlingTagService {
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CrawlingTagResDto> findAll() {
        List<Tag> tags = tagRepository.findAll();

        return tags.stream()
                .map(CrawlingTagResDto::fromEntity)
                .toList();
    }
}
