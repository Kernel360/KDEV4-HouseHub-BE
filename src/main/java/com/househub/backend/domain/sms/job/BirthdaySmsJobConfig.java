package com.househub.backend.domain.sms.job;

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
import com.househub.backend.domain.customer.entity.Customer;
import com.househub.backend.domain.customer.service.CustomerReader;
import com.househub.backend.domain.sms.dto.AligoSmsResDto;
import com.househub.backend.domain.sms.dto.SendSmsReqDto;
import com.househub.backend.domain.sms.entity.Sms;
import com.househub.backend.domain.sms.entity.SmsTemplate;
import com.househub.backend.domain.sms.enums.MessageType;
import com.househub.backend.domain.sms.enums.SmsStatus;
import com.househub.backend.domain.sms.service.AligoGateway;
import com.househub.backend.domain.sms.service.SmsTemplateReader;
import com.househub.backend.domain.sms.utils.MessageFormatter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class BirthdaySmsJobConfig {

	private final JobRepository jobRepository;
	private final PlatformTransactionManager transactionManager;

	@Bean("birthdaySmsJob")
	public Job birthdaySmsJob(Step birthdaySmsStep) {
		return new JobBuilder("birthdaySmsJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(birthdaySmsStep)
			.build();
	}

	@Bean
	public Step birthdaySmsStep(
		ItemReader<Customer> customerBirthdayReader,
		ItemProcessor<Customer, Sms> birthdaySmsProcessor,
		ItemWriter<Sms> smsLogWriter
	) {
		return new StepBuilder("birthdaySmsStep", jobRepository)
			.<Customer, Sms>chunk(100, transactionManager)
			.reader(customerBirthdayReader)
			.processor(birthdaySmsProcessor)
			.writer(smsLogWriter)
			.build();
	}

	@Bean
	@StepScope
	public ItemReader<Customer> customerBirthdayReader(CustomerReader customerReader) {
		List<Customer> data = customerReader.findAllByBirthDate();
		log.info("당일 생일자 수: {}", data.size());
		return new ListItemReader<>(data);
	}

	@Bean
	@StepScope
	public ItemProcessor<Customer, Sms> birthdaySmsProcessor(AligoGateway aligoGateway,
		MessageFormatter messageFormatter, SmsTemplateReader smsTemplateReader) {
		return customer -> {
			Agent agent = customer.getAgent();
			SmsTemplate birthdayTemplate = smsTemplateReader.findById(agent.getBirthdayTemplateId(), agent.getId());
			String msg = agent.getBirthdayTemplateId() == null ? customer.getName() + "고객님의 생일을 축하드립니다!" :
				birthdayTemplate.getContent();
			String formattedMsg = messageFormatter.addAgentInfo(msg, agent.getContact());

			SendSmsReqDto request = SendSmsReqDto.builder()
				.sender(agent.getContact())
				.receiver(customer.getContact())
				.msg(formattedMsg)
				.msgType(formattedMsg.length() > 90 ? MessageType.LMS : MessageType.SMS)
				.build();

			AligoSmsResDto aligoResponse = aligoGateway.addParamsAndSend(request);
			if (aligoResponse.getResultCode() == 1) {
				return request.toEntity(SmsStatus.SUCCESS, agent, birthdayTemplate);
			} else {
				return request.toEntity(SmsStatus.FAIL, agent, birthdayTemplate);
			}
		};
	}
}
