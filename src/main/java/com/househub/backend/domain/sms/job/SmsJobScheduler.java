package com.househub.backend.domain.sms.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SmsJobScheduler {

	private final JobLauncher jobLauncher;

	@Qualifier("smsResendJob")
	private final Job smsResendJob;

	@Scheduled(cron = "0 15 15 * * *")
	public void runSmsResendJob() {
		try {
			JobParameters parameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();

			jobLauncher.run(smsResendJob, parameters);
		} catch (Exception e) {
			log.error("SMS 재전솜 Job 실행 실패", e);
		}
	}
}
