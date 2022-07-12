package ru.example.animals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("classpath:application.yaml")
public class AnimalsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimalsApplication.class, args);
    }
}
