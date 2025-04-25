package com.househub.backend.domain.sms.job;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.enums.SmsStatus;
import com.househub.backend.domain.sms.service.SmsExecutor;
import com.househub.backend.domain.sms.service.SmsReader;
import com.househub.backend.domain.sms.service.SmsStore;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SmsResendJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;  // ✅ 트랜잭션 매니저 주입

	@Bean("smsResendJob")
	public Job smsResendJob(Step smsResendStep) {
		return new JobBuilder("smsResendJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(smsResendStep)
			.build();
	}

	@Bean
	public Step smsResendStep(
		ItemReader<Sms> smsFailLogReader,
		ItemProcessor<Sms, Sms> smsResendProcessor,
		ItemWriter<Sms> smsLogWriter
	) {
		return new StepBuilder("smsResendStep", jobRepository)
			.<Sms, Sms>chunk(100, transactionManager)  // 청크 크기 설정
			.reader(smsFailLogReader)
			.processor(smsResendProcessor)
			.writer(smsLogWriter)
			.build();
	}

	@Bean
	@StepScope  // ✅ Step 실행 시점에 빈 생성
	public ItemReader<Sms> smsFailLogReader(SmsReader smsReader) {
		List<Sms> data = smsReader.findFailLogsForResend();
		log.info("재전송 대상 SMS 건수: {}", data.size());
		return new ListItemReader<>(data);
	}

	@Bean
	@StepScope
	public ItemProcessor<Sms, Sms> smsResendProcessor(SmsExecutor smsExecutor, EntityManager entityManager) {
		return sms -> {
			Sms detachedSms = entityManager.merge(sms);
			entityManager.detach(detachedSms);

			boolean result = smsExecutor.resend(detachedSms);
			detachedSms.updateStatus((result ? SmsStatus.SUCCESS : SmsStatus.FAIL));
			detachedSms.incrementRetryCount();
			if(!result && detachedSms.getRetryCount() > 3) {
				detachedSms.updateStatus(SmsStatus.PERMANENT_FAIL);
			}
			return detachedSms;
		};
	}


	@Bean
	@StepScope
	public ItemWriter<Sms> smsLogWriter(SmsStore smsStore, EntityManager entityManager) {
		return items -> {
			// ✅ detached 엔티티 병합 후 저장
			List<Sms> mergedSmsList = items.getItems().stream()
				.map(entityManager::merge)
				.collect(Collectors.toCollection(ArrayList::new));

			smsStore.updateAll(mergedSmsList);
		};
	}

}