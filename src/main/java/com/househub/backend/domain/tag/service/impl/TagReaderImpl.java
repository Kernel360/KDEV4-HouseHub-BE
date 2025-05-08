package com.househub.backend.domain.tag.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.tag.entity.Tag;
import com.househub.backend.domain.tag.repository.TagRepository;
import com.househub.backend.domain.tag.service.TagReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagReaderImpl implements TagReader {

	private final TagRepository tagRepository;

	@Override
	public List<Tag> findAllById(List<Long> tagIds) {
		return tagRepository.findAllById(tagIds);
	}
}
