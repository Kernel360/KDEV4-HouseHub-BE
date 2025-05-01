package com.househub.backend.domain.crawlingProperty.service;

import com.househub.backend.domain.crawlingProperty.dto.TagResDto;
import com.househub.backend.domain.crawlingProperty.entity.Tag;

import java.util.List;

public interface TagService {

    List<TagResDto> findAll();

    List<Tag> tagList(List<Long> tags);
}
