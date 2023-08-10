package com.epam.esm;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.epam.esm")
@EntityScan(basePackages = "com.epam.esm")
@EnableTransactionManagement
@EnableJpaAuditing

public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
