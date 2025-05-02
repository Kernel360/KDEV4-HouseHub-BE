package com.househub.backend.domain.tag.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.tag.entity.Tag;
import com.househub.backend.domain.tag.repository.TagRepository;
import com.househub.backend.domain.tag.service.TagStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TagStoreImpl implements TagStore {

	private final TagRepository tagRepository;

	@Override
	public List<Tag> findAllById(List<Long> tagIds) {
		return tagRepository.findAllById(tagIds);
	}
}
