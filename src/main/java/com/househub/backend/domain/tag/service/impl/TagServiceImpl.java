package com.househub.backend.domain.tag.service.impl;

import com.househub.backend.domain.tag.dto.TagResDto;
import com.househub.backend.domain.tag.entity.Tag;
import com.househub.backend.domain.tag.repository.TagRepository;
import com.househub.backend.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    @Override
    public List<TagResDto> findAll() {
        List<Tag> tags = tagRepository.findAll();

        return tags.stream()
                .map(TagResDto::fromEntity)
                .toList();
    }

    @Override
    public List<Tag> tagList(List<Long> tags) {
        return tagRepository.findAllById(tags);
    }
}
