package com.househub.backend.domain.tag.controller;

import com.househub.backend.common.response.SuccessResponse;
import com.househub.backend.domain.tag.dto.TagResDto;
import com.househub.backend.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/crawlingTag")
public class TagController {

    private final TagService tagService;

    @GetMapping("")
    public ResponseEntity<SuccessResponse<List<TagResDto>>> findAllTags() {
        List<TagResDto> response = tagService.findAll();

        return ResponseEntity.ok(SuccessResponse.success("태그 목록 조회에 성공했습니다.", "FIND_TAG_SUCCESS", response));
    }
}
