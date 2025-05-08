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

	@Qualifier("contractExpireSmsJob")
	private final Job contractExpireSmsJob;

	@Scheduled(cron = "0 0 0 * * *")
	public void runSmsResendJob() {
		executeJob(smsResendJob, "SMS 재전송 Job");
	}

	// 계약 종료일 3달 전인 고객에게 문자로 알림을 보냄
	@Scheduled(cron = "0 0 0 1 * *")
	public void runContractExpireSmsJob(){
		executeJob(contractExpireSmsJob,"계약 만료 알림 SMS 전송 Job");
	}

	private void executeJob(Job job, String jobName) {
		try {
			JobParameters parameters = new JobParametersBuilder()
				.addLong("runtime", System.currentTimeMillis())
				.toJobParameters();

			log.info("{} 시작", jobName);
			jobLauncher.run(job, parameters);
			log.info("{} 완료", jobName);
		} catch (Exception e) {
			log.error("{} 실행 실패: {}", jobName, e.getMessage());
		}
	}

	// 생일 대상자인 고객에게 축하 메세지 자동 전송


}
