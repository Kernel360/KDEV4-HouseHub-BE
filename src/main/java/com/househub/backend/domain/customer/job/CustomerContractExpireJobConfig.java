// package com.househub.backend.domain.customer.job;
//
// import java.time.LocalDateTime;
// import java.util.List;
//
// import org.springframework.batch.core.Job;
// import org.springframework.batch.core.Step;
// import org.springframework.batch.core.configuration.annotation.StepScope;
// import org.springframework.batch.core.job.builder.JobBuilder;
// import org.springframework.batch.core.launch.support.RunIdIncrementer;
// import org.springframework.batch.core.repository.JobRepository;
// import org.springframework.batch.core.step.builder.StepBuilder;
// import org.springframework.batch.item.ItemProcessor;
// import org.springframework.batch.item.ItemReader;
// import org.springframework.batch.item.ItemWriter;
// import org.springframework.batch.item.support.ListItemReader;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.transaction.PlatformTransactionManager;
//
// import com.househub.backend.domain.customer.entity.Customer;
// import com.househub.backend.domain.customer.service.CustomerExecutor;
// import com.househub.backend.domain.customer.service.CustomerReader;
// import com.househub.backend.domain.sms.entity.Sms;
// import com.househub.backend.domain.sms.service.SmsExecutor;
// import com.househub.backend.domain.sms.service.SmsStore;
//
// import jakarta.persistence.EntityManager;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Configuration
// @RequiredArgsConstructor
// public class CustomerContractExpireJobConfig {
//
// 	private final JobRepository jobRepository;
// 	private final PlatformTransactionManager transactionManager;
//
// 	@Bean("contractExpireJob")
// 	public Job contractExpireJob(Step contractExpireStep) {
// 		return new JobBuilder("contractExpireJob", jobRepository)
// 			.incrementer(new RunIdIncrementer())
// 			.start(contractExpireStep)
// 			.build();
// 	}
//
// 	@Bean
// 	public Step contractExpireStep(
// 		ItemReader<Customer> customerReader,
// 		ItemProcessor<Customer, Sms> customerProcessor,
// 		ItemWriter<Sms> smsWriter
// 	) {
// 		return new StepBuilder("contractExpireStep", jobRepository)
// 			.chunk(100, transactionManager)
// 			.reader(customerReader)
// 			.processor(customerProcessor)
// 			.writer(smsWriter)
// 			.build();
// 	}
//
// 	@Bean
// 	@StepScope
// 	public ItemReader<Customer> customerReader(CustomerReader customerReader) {
// 		LocalDateTime today = LocalDateTime.now();
// 		List<Customer> data = customerReader.findAllByContractEndDate(today);
// 		log.info("계약 만료 예정 고객 수: {}", data.size());
// 		return new ListItemReader<>(data);
// 	}
//
// 	@Bean
// 	@StepScope
// 	public ItemProcessor<Customer, Sms> customerProcessor(
// 		SmsExecutor smsExecutor, EntityManager entityManager) {
// 		// 고객의 계약 만료일이 오늘인 경우 처리// 안내 문자 발송 로직
// 		return smsExecutor::sendContractExpireNotification;
// 	}
//
// 	@Bean
// 	@StepScope
// 	public ItemWriter<Sms> smsWriter(SmsStore smsStore, EntityManager entityManager) {
// 		return items -> {
// 			for (Sms sms : items) {
// 				if (sms != null) {
// 					smsStore.create(sms);
// 				}
// 			}
// 		};
// 	}
// }