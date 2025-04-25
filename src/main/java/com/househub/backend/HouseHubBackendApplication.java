package com.househub.backend;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableRedisHttpSession // Redis를 세션저장소로 사용
@EnableScheduling
public class HouseHubBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseHubBackendApplication.class, args);
	}

	@PostConstruct
	public void setTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

}
