package com.miracle.release.application;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
@EnableSwagger2
@ComponentScan(basePackages = "com.miracle")
public class ReleasePlanApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReleasePlanApplication.class, args);
	}
	@Bean
	public Docket productApi() {
	return new Docket(DocumentationType.SWAGGER_2)
	.select() .apis(RequestHandlerSelectors.basePackage("com.miracle"))
	.build();
	}
}
