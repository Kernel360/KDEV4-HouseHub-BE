package com.househub.backend.domain.inquiryTemplate.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.househub.backend.common.exception.AlreadyExistsException;
import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.service.AgentReader;
import com.househub.backend.domain.inquiryTemplate.dto.CreateInquiryTemplateReqDto;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplate;
import com.househub.backend.domain.inquiryTemplate.entity.InquiryTemplateSharedToken;
import com.househub.backend.domain.inquiryTemplate.entity.Question;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateCreateExecutor;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateReader;
import com.househub.backend.domain.inquiryTemplate.service.InquiryTemplateStore;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InquiryTemplateCreateExecutorImpl implements InquiryTemplateCreateExecutor {
	private final AgentReader agentReader;
	private final InquiryTemplateReader reader;
	private final InquiryTemplateStore store;

	@Override
	public void execute(CreateInquiryTemplateReqDto reqDto, Long agentId) {
		Agent agent = agentReader.findById(agentId);

		// 동일한 이름의 문의 템플릿이 이미 존재하는지 확인
		boolean exists = reader.existsByAgentIdAndName(agentId,
			reqDto.getName());
		if (exists) {
			throw new AlreadyExistsException("이미 동일한 이름의 문의 템플릿이 존재합니다.", "DUPLICATED_INQUIRY_TEMPLATE_NAME");
		}

		// 문의 템플릿 생성
		InquiryTemplate inquiryTemplate = InquiryTemplate.fromDto(reqDto, agent);
		store.createTemplate(inquiryTemplate);

		// 질문 목록 생성 및 저장
		List<Question> questions = reqDto.getQuestions().stream()
			.map(questionDto -> Question.fromDto(questionDto, inquiryTemplate))
			.collect(Collectors.toList());
		store.createQuestions(questions);

		// 공유 토큰 저장 (InquiryTemplateSharedToken)
		// 문의 템플릿 생성 시 공유 토큰도 무조건 함께 생성하되, 활성화 여부는 템플릿의 isActive 상태를 반영한다.
		// 공유 토큰은 고객이 해당 템플릿에 접근할 수 있는 식별자이며, 관리자가 공유 여부를 토글하여 사용 여부를 제어할 수 있다.
		InquiryTemplateSharedToken token = InquiryTemplateSharedToken.create(inquiryTemplate);
		token.setActive(inquiryTemplate.getActive());
		store.createToken(token);
	}
}
