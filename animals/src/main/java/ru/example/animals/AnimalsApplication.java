package ru.example.animals;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"ru.example.animals"})
@EnableJpaRepositories(basePackages = {"ru.example.animals"})
@EntityScan(basePackages = {"ru.example.animals"})
public class AnimalsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnimalsApplication.class, args);
    }

}
