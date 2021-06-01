package com.ros.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import com.ros.accounting.cashup.mapper.RestDtoMapper;
import com.ros.accounting.cashup.mapper.RestDtoMapperImpl;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableEurekaClient
@OpenAPIDefinition(info = @Info(title = "AccountingServiceAPI", version = "1.0", description = "API for Accounting Service"))
public class AccountingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountingServiceApplication.class, args);
	}

	@Bean
	public RestDtoMapper mapper() {
		return new RestDtoMapperImpl();
	}
}
