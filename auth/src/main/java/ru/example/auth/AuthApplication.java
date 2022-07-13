package ru.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(
//        scanBasePackages = {"ru.example.auth"})
//@EnableJpaRepositories(basePackages = {"ru.example.auth"})
//@EntityScan(basePackages = {"ru.example.auth"})
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

}
