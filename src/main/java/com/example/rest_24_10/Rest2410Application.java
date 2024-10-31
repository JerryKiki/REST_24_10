package com.example.rest_24_10;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class Rest2410Application {
    public static void main(String[] args) {
        SpringApplication.run(Rest2410Application.class, args);
    }
}
