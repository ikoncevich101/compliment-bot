package com.darya.compiment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ComplimentBot {
    public static void main(String[] args) {
        SpringApplication.run(ComplimentBot.class, args);
    }
}
