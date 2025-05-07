package com.househub.backend.domain.tag.service;

import com.househub.backend.domain.tag.dto.TagResDto;
import com.househub.backend.domain.tag.entity.Tag;

import java.util.List;

public interface TagService {

    List<TagResDto> findAll();

    List<Tag> tagList(List<Long> tags);
}
