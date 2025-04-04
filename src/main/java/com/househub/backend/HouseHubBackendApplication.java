package com.househub.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@EnableRedisHttpSession // Redis를 세션저장소로 사용
public class HouseHubBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HouseHubBackendApplication.class, args);
	}

}
