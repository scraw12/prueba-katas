package com.alejandro.test;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.alejandro.test.application.repository")
@EnableTransactionManagement
public class TestAlejandroApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestAlejandroApplication.class, args);
    }
}
