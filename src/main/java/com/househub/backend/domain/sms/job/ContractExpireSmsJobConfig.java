package com.househub.backend.domain.sms.job;

import java.time.LocalDate;
import java.util.List;

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

import com.househub.backend.domain.agent.entity.Agent;
import com.househub.backend.domain.contract.entity.Contract;
import com.househub.backend.domain.contract.service.ContractReader;
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.sms.dto.AligoSmsResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.enums.MessageType;
import com.househub.backend.domain.sms.enums.SmsStatus;
import com.househub.backend.domain.sms.service.AligoGateway;
import com.househub.backend.domain.sms.utils.MessageFormatter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ContractExpireSmsJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean("contractExpireSmsJob")
	public Job contractExpireSmsJob(Step contractExpireSmsStep) {
		return new JobBuilder("contractExpireSmsJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(contractExpireSmsStep)
			.build();
	}

	@Bean
	public Step contractExpireSmsStep(
		ItemReader<Contract> contractExpireReader,
		ItemProcessor<Contract, Sms> expireSmsProcessor,
		ItemWriter<Sms> smsLogWriter
	) {
		return new StepBuilder("contractExpireSmsStep", jobRepository)
			.<Contract, Sms>chunk(100, transactionManager)
			.reader(contractExpireReader)
			.processor(expireSmsProcessor)
			.writer(smsLogWriter)
			.build();
	}

	@Bean
	@StepScope
	public ItemReader<Contract> contractExpireReader(ContractReader contractReader) {
		LocalDate today = LocalDate.now();
		LocalDate startDate = today.plusMonths(3).withDayOfMonth(1);
		LocalDate endDate = today.plusMonths(3).withDayOfMonth(today.plusMonths(3).lengthOfMonth());
		List<Contract> data = contractReader.findAllByExpiredAtBetween(startDate, endDate);
		log.info("계약 종료 안내 SMS 건수: {}", data.size());
		return new ListItemReader<>(data);
	}

	@Bean
	@StepScope
	public ItemProcessor<Contract, Sms> expireSmsProcessor(AligoGateway aligoGateway,
		MessageFormatter messageFormatter) {
		return contract -> {
			Agent agent = contract.getAgent();
			Customer customer = contract.getCustomer();
			SendSmsReqDto request = SendSmsReqDto.builder()
				.sender(agent.getContact())
				.receiver(customer.getContact())
				.msg(messageFormatter.addAgentInfo("고객님의 계약이 3개월 후 만료됩니다!", agent.getContact()))
				.msgType(MessageType.SMS)
				.build();

			// SMS 발송 시도
			AligoSmsResDto aligoResponse = aligoGateway.addParamsAndSend(request);
			if (aligoResponse.getResultCode() == 1) {
				return request.toEntity(SmsStatus.SUCCESS, agent, null);
			} else {
				// 실패 시, 실패 상태로 저장하거나 null 반환 (null 반환 시 Writer로 넘어가지 않음)
				return request.toEntity(SmsStatus.FAIL, agent, null);
			}
		};
	}
}
