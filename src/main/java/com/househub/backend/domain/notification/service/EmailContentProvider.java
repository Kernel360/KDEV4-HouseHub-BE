package com.househub.backend.domain.notification.service;

import java.util.Map;

public interface EmailContentProvider {
	String getSubject();

	String getBody(Map<String, String> variables);
}

