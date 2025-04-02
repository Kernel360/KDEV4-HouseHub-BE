package com.househub.backend.domain.sms.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.StandardCharsets;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        // 기본 컨버터 제거 후 UTF-8 컨버터 추가 [2][7]
        restTemplate.getMessageConverters().removeIf(
                c -> c instanceof StringHttpMessageConverter
        );
        restTemplate.getMessageConverters().add(0,
                new StringHttpMessageConverter(StandardCharsets.UTF_8)
        );

        return restTemplate;
    }
}
