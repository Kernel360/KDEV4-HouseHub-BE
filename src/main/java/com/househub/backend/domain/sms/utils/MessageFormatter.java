package com.househub.backend.domain.sms.utils;

import org.springframework.stereotype.Component;

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.agent.service.AgentReader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MessageFormatter {

	private final AgentReader agentReader;

	public String addAgentInfo(String originMsg, String sender){
		Agent agent = agentReader.findByContact(sender);
		if (agent == null) {
			// 예외 처리 또는 기본 메시지 반환
			return "알 수 없는 보낸이\n\n" + originMsg + "\n\n문의 번호: " + sender;
		}

		String senderName = agent.getName() != null ? agent.getName() : "알 수 없는 중개인";
		String realEstateName = agent.getRealEstate() != null ? agent.getRealEstate().getName() : null;

		StringBuilder sb = new StringBuilder();
		sb.append("보낸이: ");
		if (realEstateName != null) {
			sb.append(realEstateName).append(" ");
		}
		sb.append(senderName)
			.append("\n\n")
			.append(originMsg)
			.append("\n\n문의 번호: ")
			.append(sender);

		return sb.toString();
	}
}
