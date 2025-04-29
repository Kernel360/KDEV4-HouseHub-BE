// package com.househub.backend.domain.customer.job;
//
// import java.time.LocalDate;
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
// import com.househub.backend.domain.customer.service.CustomerStore;
//
// import jakarta.persistence.EntityManager;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
//
// @Slf4j
// @Configuration
// @RequiredArgsConstructor
// public class CustomerBirthdayJobConfig {
//
// 	private final JobRepository jobRepository;
// 	private final PlatformTransactionManager transactionManager;
//
// 	@Bean("customerBirthdayJob")
// 	public Job birthdayJob(Step birthDayStep) {
// 		return new JobBuilder("customerBirthdayJob", jobRepository)
// 			.incrementer(new RunIdIncrementer())
// 			.start(birthDayStep)
// 			.build();
// 	}
//
// 	@Bean
// 	public Step birthdayStep(
// 		ItemReader<Customer> customerReader,
// 		ItemProcessor<Customer, Customer> customerProcessor,
// 		ItemWriter<Customer> customerWriter
// 	) {
// 		return new StepBuilder("customerBirthdayStep", jobRepository)
// 			.chunk(100,transactionManager)
// 			.reader(customerReader)
// 			.processor(customerProcessor)
// 			.writer(customerWriter)
// 			.build();
// 	}
//
// 	@Bean
// 	@StepScope
// 	public ItemReader<Customer> customerReader(CustomerReader customerReader) {
// 		LocalDate today = LocalDate.now();
// 		List<Customer> data = customerReader.findAllByBirthDate(today);
// 		// 고객을 담당하는 Agent를 확인
// 		log.info("총 생일자 수: {}",data.size());
// 		return new ListItemReader<>(data);
// 	}
//
// 	@Bean
// 	@StepScope
// 	public ItemProcessor<Customer, Customer> customerProcessor(CustomerExecutor customerExecutor, EntityManager entityManager){
// 		return customer -> {
// 		}
// 	}
//
// 	@Bean
// 	@StepScope
// 	public ItemWriter<Customer> customerWriter(CustomerStore customerStore, EntityManager entityManager){
// 		return items -> {
//
// 		}
// 	}
// }
