package com.househub.backend.domain.inquiryForm.service.impl;

import com.househub.backend.domain.inquiryForm.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplateListResDto;
import com.househub.backend.domain.inquiryForm.dto.InquiryTemplateResDto;
import com.househub.backend.domain.inquiryForm.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryForm.entity.Question;
import com.househub.backend.domain.inquiryForm.repository.InquiryTemplateRepository;
import com.househub.backend.domain.inquiryForm.repository.QuestionRepository;
import com.househub.backend.domain.inquiryForm.service.InquiryTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryTemplateServiceImpl implements InquiryTemplateService {
    private final InquiryTemplateRepository inquiryTemplateRepository;
    private final QuestionRepository questionRepository;

    @Override
    @Transactional
    public void createNewInquiryTemplate(CreateInquiryTemplateReqDto reqDto) {
        // 1. 문의 템플릿 생성
        InquiryTemplate inquiryTemplate = InquiryTemplate.fromDto(reqDto);
        inquiryTemplateRepository.save(inquiryTemplate);

        // 2. 질문 목록 생성 및 저장
        List<Question> questions = reqDto.getQuestions().stream()
                .map(questionDto -> Question.fromDto(questionDto, inquiryTemplate))
                .collect(Collectors.toList());
        questionRepository.saveAll(questions);
    }

    @Override
    public InquiryTemplateListResDto getInquiryTemplates(Boolean isActive, Pageable pageable) {
        Page<InquiryTemplateResDto> page = inquiryTemplateRepository.findAllByFilters(isActive, pageable)
                .map(InquiryTemplateResDto::fromEntity);

        return InquiryTemplateListResDto.fromPage(page);
    }
}
